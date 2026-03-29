<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../utils/api'

const courses = ref([])
const teachers = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({ name: '', code: '', credit: 1, maxCapacity: 50, teacherId: null, schedule: '', classroom: '', description: '' })

const loadCourses = async () => {
  try {
    const res = await api.get('/courses')
    courses.value = res.data
  } catch (e) {}
}

const loadTeachers = async () => {
  try {
    const res = await api.get('/admin/users/role/TEACHER')
    teachers.value = res.data
  } catch (e) {}
}

const openCreate = () => {
  isEdit.value = false
  form.value = { name: '', code: '', credit: 1, maxCapacity: 50, teacherId: null, schedule: '', classroom: '', description: '' }
  dialogVisible.value = true
}

const openEdit = (course) => {
  isEdit.value = true
  form.value = {
    id: course.id,
    name: course.name,
    code: course.code,
    credit: course.credit,
    maxCapacity: course.maxCapacity,
    teacherId: course.teacher?.id || null,
    schedule: course.schedule || '',
    classroom: course.classroom || '',
    description: course.description || ''
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await api.put(`/courses/${form.value.id}`, form.value)
      ElMessage.success('更新成功')
    } else {
      await api.post('/courses', form.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadCourses()
  } catch (e) {}
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该课程？', '提示', { type: 'warning' })
    await api.delete(`/courses/${id}`)
    ElMessage.success('删除成功')
    loadCourses()
  } catch (e) {}
}

onMounted(() => {
  loadCourses()
  loadTeachers()
})
</script>

<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center">
          <span>课程管理</span>
          <el-button type="primary" @click="openCreate">添加课程</el-button>
        </div>
      </template>
      <el-table :data="courses" stripe border>
        <el-table-column prop="code" label="编号" width="120" />
        <el-table-column prop="name" label="名称" width="160" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column label="教师" width="120">
          <template #default="{ row }">{{ row.teacher?.realName || '未分配' }}</template>
        </el-table-column>
        <el-table-column prop="schedule" label="上课时间" width="150" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column label="人数" width="100">
          <template #default="{ row }">{{ row.currentCount }} / {{ row.maxCapacity }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑课程' : '添加课程'" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="课程名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="form.code" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input-number v-model="form.credit" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="最大容量">
          <el-input-number v-model="form.maxCapacity" :min="1" :max="500" />
        </el-form-item>
        <el-form-item label="授课教师">
          <el-select v-model="form.teacherId" placeholder="选择教师" clearable style="width:100%">
            <el-option v-for="t in teachers" :key="t.id" :label="t.realName" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="上课时间">
          <el-input v-model="form.schedule" placeholder="如：周一 1-2节" />
        </el-form-item>
        <el-form-item label="教室">
          <el-input v-model="form.classroom" placeholder="如：A301" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
