<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../utils/api'

const users = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({ username: '', password: '', realName: '', role: 'STUDENT', email: '', phone: '' })
const roleFilter = ref('')

const loadUsers = async () => {
  try {
    const res = roleFilter.value
      ? await api.get(`/admin/users/role/${roleFilter.value}`)
      : await api.get('/admin/users')
    users.value = res.data
  } catch (e) {}
}

const openCreate = () => {
  isEdit.value = false
  form.value = { username: '', password: '', realName: '', role: 'STUDENT', email: '', phone: '' }
  dialogVisible.value = true
}

const openEdit = (user) => {
  isEdit.value = true
  form.value = { id: user.id, username: user.username, password: '', realName: user.realName, role: user.role, email: user.email || '', phone: user.phone || '' }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await api.put(`/admin/users/${form.value.id}`, form.value)
      ElMessage.success('更新成功')
    } else {
      await api.post('/admin/users', form.value)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadUsers()
  } catch (e) {}
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该用户？', '提示', { type: 'warning' })
    await api.delete(`/admin/users/${id}`)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (e) {}
}

const roleLabel = (role) => {
  const map = { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }
  return map[role] || role
}

onMounted(loadUsers)
</script>

<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex; justify-content:space-between; align-items:center">
          <span>用户管理</span>
          <div>
            <el-select v-model="roleFilter" placeholder="筛选角色" clearable style="width:120px; margin-right:10px" @change="loadUsers">
              <el-option label="全部" value="" />
              <el-option label="管理员" value="ADMIN" />
              <el-option label="教师" value="TEACHER" />
              <el-option label="学生" value="STUDENT" />
            </el-select>
            <el-button type="primary" @click="openCreate">添加用户</el-button>
          </div>
        </div>
      </template>
      <el-table :data="users" stripe border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 'ADMIN' ? 'danger' : row.role === 'TEACHER' ? 'warning' : 'success'">
              {{ roleLabel(row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑用户' : '添加用户'" v-model="dialogVisible" width="450px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item :label="isEdit ? '新密码' : '密码'">
          <el-input v-model="form.password" type="password" :placeholder="isEdit ? '留空则不修改' : '请输入密码'" show-password />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" :disabled="isEdit" style="width:100%">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="教师" value="TEACHER" />
            <el-option label="学生" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
