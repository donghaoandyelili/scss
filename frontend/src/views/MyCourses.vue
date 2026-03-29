<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../utils/api'

const selections = ref([])

const loadSelections = async () => {
  try {
    const res = await api.get('/selections/my')
    selections.value = res.data
  } catch (e) {}
}

const handleDrop = async (courseId) => {
  try {
    await ElMessageBox.confirm('确认退选该课程？', '提示', { type: 'warning' })
    await api.delete(`/selections/${courseId}`)
    ElMessage.success('退课成功')
    loadSelections()
  } catch (e) {}
}

onMounted(loadSelections)
</script>

<template>
  <div class="my-course-page page-section">
    <section class="selection-summary glass-panel">
      <div>
        <div class="section-title">我的选课</div>
        <div class="section-subtitle">这里集中展示你已锁定的课程与时间安排。</div>
      </div>
      <div class="summary-number">{{ selections.length }}</div>
    </section>

    <el-card>
      <template #header><span>已选课程列表</span></template>
      <el-empty v-if="selections.length === 0" description="暂未选课" />
      <el-table v-else :data="selections" stripe border>
        <el-table-column label="课程编号" width="120">
          <template #default="{ row }">{{ row.course.code }}</template>
        </el-table-column>
        <el-table-column label="课程名称" width="160">
          <template #default="{ row }">{{ row.course.name }}</template>
        </el-table-column>
        <el-table-column label="学分" width="80">
          <template #default="{ row }">{{ row.course.credit }}</template>
        </el-table-column>
        <el-table-column label="授课教师" width="120">
          <template #default="{ row }">{{ row.course.teacher?.realName || '未分配' }}</template>
        </el-table-column>
        <el-table-column label="上课时间" width="150">
          <template #default="{ row }">{{ row.course.schedule }}</template>
        </el-table-column>
        <el-table-column label="教室" width="120">
          <template #default="{ row }">{{ row.course.classroom }}</template>
        </el-table-column>
        <el-table-column label="选课时间">
          <template #default="{ row }">{{ row.selectTime }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="handleDrop(row.course.id)">退课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.my-course-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.selection-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 26px 28px;
  border-radius: 26px;
}

.summary-number {
  min-width: 96px;
  height: 96px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 28px;
  background: linear-gradient(135deg, rgba(40, 123, 255, 0.3), rgba(32, 214, 176, 0.2));
  border: 1px solid rgba(154, 184, 255, 0.16);
  color: #f7fbff;
  font-size: 40px;
  font-weight: 800;
}

@media (max-width: 760px) {
  .selection-summary {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }
}
</style>
