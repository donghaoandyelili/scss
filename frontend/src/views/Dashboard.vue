<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import api from '../utils/api'

const userStore = useUserStore()
const stats = ref({ courseCount: 0, selectedCount: 0 })

onMounted(async () => {
  try {
    const res = await api.get('/courses')
    stats.value.courseCount = res.data.length
  } catch (e) {}

  if (userStore.role === 'STUDENT') {
    try {
      const res = await api.get('/selections/my')
      stats.value.selectedCount = res.data.length
    } catch (e) {}
  }
})
</script>

<template>
  <div class="dashboard page-section">
    <section class="hero-banner glass-panel">
      <div>
        <div class="hero-badge">LIVE DASHBOARD</div>
        <h2>欢迎回来，{{ userStore.user?.realName }}</h2>
        <p>把课程、选课人数与身份权限压缩成一眼就能读懂的仪表视图。</p>
      </div>
      <div class="hero-orbit">
        <div class="orbit-core">{{ stats.courseCount }}</div>
        <span>课程在线</span>
      </div>
    </section>

    <div class="stats-grid">
      <el-card shadow="hover" class="metric-card metric-card-blue">
        <template #header><span>可选课程总数</span></template>
        <div class="metric-value">{{ stats.courseCount }}</div>
        <div class="metric-foot">当前系统内已开放浏览的课程数量</div>
      </el-card>

      <el-card shadow="hover" class="metric-card metric-card-green" v-if="userStore.role === 'STUDENT'">
        <template #header><span>已选课程数</span></template>
        <div class="metric-value">{{ stats.selectedCount }}</div>
        <div class="metric-foot">你当前已成功提交的课程选择</div>
      </el-card>

      <el-card shadow="hover" class="metric-card metric-card-purple">
        <template #header><span>当前角色</span></template>
        <div class="metric-role">{{ userStore.role === 'ADMIN' ? '管理员' : userStore.role === 'TEACHER' ? '教师' : '学生' }}</div>
        <div class="metric-foot">不同角色会看到不同的数据与操作入口</div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.hero-banner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 34px;
  border-radius: 28px;
}

.hero-badge {
  display: inline-flex;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.05);
  color: #93bbff;
  font-size: 12px;
  letter-spacing: 0.16em;
}

.hero-banner h2 {
  margin-top: 18px;
  font-size: 42px;
  color: #f7fbff;
}

.hero-banner p {
  max-width: 600px;
  margin-top: 14px;
  color: rgba(220, 230, 255, 0.72);
  line-height: 1.8;
}

.hero-orbit {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: radial-gradient(circle, rgba(68, 139, 255, 0.26), rgba(255, 255, 255, 0.03));
  border: 1px solid rgba(151, 182, 255, 0.16);
  box-shadow: inset 0 0 60px rgba(82, 126, 255, 0.14);
}

.orbit-core {
  font-size: 54px;
  font-weight: 800;
  color: #f8fbff;
}

.hero-orbit span {
  margin-top: 6px;
  color: #9bbdff;
  letter-spacing: 0.12em;
  font-size: 12px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
}

.metric-card {
  min-height: 220px;
}

.metric-value,
.metric-role {
  margin-top: 18px;
  font-size: 56px;
  font-weight: 800;
  line-height: 1;
  color: #f7fbff;
}

.metric-role {
  font-size: 42px;
}

.metric-foot {
  margin-top: 18px;
  color: rgba(216, 228, 255, 0.68);
  line-height: 1.7;
}

.metric-card-blue {
  background: linear-gradient(135deg, rgba(38, 118, 255, 0.28), rgba(11, 18, 35, 0.92)) !important;
}

.metric-card-green {
  background: linear-gradient(135deg, rgba(19, 180, 146, 0.28), rgba(11, 18, 35, 0.92)) !important;
}

.metric-card-purple {
  background: linear-gradient(135deg, rgba(104, 98, 255, 0.28), rgba(11, 18, 35, 0.92)) !important;
}

@media (max-width: 1100px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .hero-banner {
    flex-direction: column;
    align-items: flex-start;
    gap: 24px;
    padding: 24px;
  }

  .hero-banner h2 {
    font-size: 32px;
  }

  .hero-orbit {
    width: 140px;
    height: 140px;
  }

  .orbit-core,
  .metric-value {
    font-size: 42px;
  }
}
</style>
