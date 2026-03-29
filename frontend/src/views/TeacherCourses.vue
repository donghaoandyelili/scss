<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import api from '../utils/api'

const userStore = useUserStore()
const courses = ref([])
const selectedCourse = ref(null)
const students = ref([])

const loadCourses = async () => {
  try {
    const res = await api.get(`/courses/teacher/${userStore.user.id}`)
    courses.value = res.data
  } catch (e) {}
}

const viewStudents = async (course) => {
  selectedCourse.value = course
  try {
    const res = await api.get(`/selections/course/${course.id}`)
    students.value = res.data
  } catch (e) {}
}

onMounted(loadCourses)
</script>

<template>
  <div>
    <el-card>
      <template #header><span>我的授课</span></template>
      <el-empty v-if="courses.length === 0" description="暂无授课课程" />
      <el-table v-else :data="courses" stripe border @row-click="viewStudents" style="cursor:pointer">
        <el-table-column prop="code" label="课程编号" width="120" />
        <el-table-column prop="name" label="课程名称" width="200" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="schedule" label="上课时间" width="150" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column label="选课人数" width="120">
          <template #default="{ row }">{{ row.currentCount }} / {{ row.maxCapacity }}</template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-card v-if="selectedCourse" style="margin-top: 20px">
      <template #header><span>{{ selectedCourse.name }} - 选课学生列表</span></template>
      <el-empty v-if="students.length === 0" description="暂无学生选课" />
      <el-table v-else :data="students" stripe border>
        <el-table-column label="学生姓名" width="150">
          <template #default="{ row }">{{ row.student.realName }}</template>
        </el-table-column>
        <el-table-column label="用户名" width="150">
          <template #default="{ row }">{{ row.student.username }}</template>
        </el-table-column>
        <el-table-column label="邮箱">
          <template #default="{ row }">{{ row.student.email }}</template>
        </el-table-column>
        <el-table-column label="选课时间" width="200">
          <template #default="{ row }">{{ row.selectTime }}</template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
