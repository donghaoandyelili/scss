<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../utils/api'

const userStore = useUserStore()
const courses = ref([])
const searchName = ref('')

const loadCourses = async () => {
  try {
    const res = searchName.value
      ? await api.get('/courses/search', { params: { name: searchName.value } })
      : await api.get('/courses')
    courses.value = res.data
  } catch (e) {}
}

const handleSelect = async (courseId) => {
  try {
    await ElMessageBox.confirm('确认选择该课程？', '提示', { type: 'warning' })
    await api.post(`/selections/${courseId}`)
    ElMessage.success('选课成功')
    loadCourses()
  } catch (e) {}
}

onMounted(loadCourses)
</script>

<template>
  <div class="course-page page-section">
    <section class="course-topbar glass-panel">
      <div>
        <div class="section-title">课程矩阵</div>
        <div class="section-subtitle">把课程状态、容量与教师分配压成一张更易读的操作面板。</div>
      </div>
      <div class="topbar-meta">
        <span class="data-chip">共 {{ courses.length }} 门课程</span>
        <span class="data-chip" v-if="userStore.role === 'STUDENT'">学生选课模式</span>
      </div>
    </section>

    <el-card>
      <template #header>
        <div class="course-toolbar">
          <span>课程列表</span>
          <div class="search-group">
            <el-input v-model="searchName" placeholder="搜索课程名称" class="search-input" clearable @clear="loadCourses" />
            <el-button type="primary" @click="loadCourses">搜索</el-button>
          </div>
        </div>
      </template>
      <el-table :data="courses" stripe border>
        <el-table-column prop="code" label="课程编号" width="120" />
        <el-table-column prop="name" label="课程名称" width="160" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column label="授课教师" width="120">
          <template #default="{ row }">{{ row.teacher?.realName || '未分配' }}</template>
        </el-table-column>
        <el-table-column prop="schedule" label="上课时间" width="150" />
        <el-table-column prop="classroom" label="教室" width="120" />
        <el-table-column label="选课人数" width="120">
          <template #default="{ row }">{{ row.currentCount }} / {{ row.maxCapacity }}</template>
        </el-table-column>
        <el-table-column prop="description" label="课程描述" show-overflow-tooltip />
        <el-table-column label="操作" width="100" v-if="userStore.role === 'STUDENT'">
          <template #default="{ row }">
            <el-button type="success" size="small" @click="handleSelect(row.id)"
                       :disabled="row.currentCount >= row.maxCapacity">
              {{ row.currentCount >= row.maxCapacity ? '已满' : '选课' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.course-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.course-topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  padding: 26px 28px;
  border-radius: 26px;
}

.topbar-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.course-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.search-group {
  display: flex;
  gap: 10px;
}

.search-input {
  width: 240px;
}

@media (max-width: 760px) {
  .course-topbar,
  .course-toolbar,
  .search-group {
    flex-direction: column;
    align-items: stretch;
  }

  .search-input {
    width: 100%;
  }
}
</style>
