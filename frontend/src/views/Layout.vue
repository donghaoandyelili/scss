<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()
const roleLabel = computed(() => {
  if (userStore.role === 'ADMIN') return '管理员'
  if (userStore.role === 'TEACHER') return '教师'
  return '学生'
})

const menuItems = computed(() => {
  const items = [
    { path: '/dashboard', title: '首页', icon: 'HomeFilled' },
    { path: '/courses', title: '课程列表', icon: 'Reading' }
  ]
  if (userStore.role === 'STUDENT') {
    items.push({ path: '/my-courses', title: '我的选课', icon: 'Collection' })
  }
  if (userStore.role === 'TEACHER') {
    items.push({ path: '/teacher-courses', title: '我的授课', icon: 'Notebook' })
  }
  if (userStore.role === 'ADMIN') {
    items.push({ path: '/course-manage', title: '课程管理', icon: 'Setting' })
    items.push({ path: '/user-manage', title: '用户管理', icon: 'UserFilled' })
  }
  return items
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="layout-shell app-shell">
    <el-container class="layout-container page-section">
      <el-aside width="260px" class="layout-aside glass-panel">
        <div class="logo-block">
          <span class="logo-mini">Enrollment OS</span>
          <h3>学生选课系统</h3>
          <p>更顺滑的课程流转体验</p>
        </div>
        <div class="role-card">
          <span>当前身份</span>
          <strong>{{ roleLabel }}</strong>
          <em>{{ userStore.user?.realName }}</em>
        </div>
      <el-menu
        :default-active="$route.path"
        background-color="transparent"
        text-color="#b8c8f0"
        active-text-color="#ffffff"
        class="side-menu"
        router
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
      </el-aside>
      <el-container class="layout-main-wrap">
        <el-header class="layout-header glass-panel">
          <div>
            <div class="header-kicker">COURSE CONTROL CENTER</div>
            <div class="header-title">{{ $route.meta?.title || '首页' }}</div>
          </div>
          <div class="header-actions">
            <div class="user-pill">
              <span>{{ userStore.user?.realName }}</span>
              <strong>{{ roleLabel }}</strong>
            </div>
            <el-button type="danger" @click="handleLogout">退出登录</el-button>
          </div>
        </el-header>
        <el-main class="layout-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.layout-shell {
  min-height: 100vh;
  padding: 22px;
}

.layout-container {
  min-height: calc(100vh - 44px);
  gap: 18px;
}

.layout-aside {
  padding: 24px 18px;
  border-radius: 30px;
}

.logo-block {
  padding: 12px 10px 20px;
}

.logo-mini {
  display: inline-flex;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.05);
  color: #8fb6ff;
  font-size: 11px;
  letter-spacing: 0.14em;
}

.logo-block h3 {
  margin-top: 18px;
  font-size: 28px;
  color: #f6fbff;
}

.logo-block p {
  margin-top: 8px;
  color: rgba(214, 226, 252, 0.68);
  font-size: 13px;
}

.role-card {
  margin: 4px 10px 18px;
  padding: 18px;
  border-radius: 20px;
  background: linear-gradient(135deg, rgba(38, 108, 255, 0.24), rgba(44, 211, 176, 0.14));
  border: 1px solid rgba(152, 184, 255, 0.18);
}

.role-card span,
.role-card em {
  display: block;
  color: rgba(224, 233, 255, 0.72);
}

.role-card strong {
  display: block;
  margin-top: 10px;
  font-size: 28px;
  color: #f7fbff;
}

.role-card em {
  margin-top: 8px;
  font-style: normal;
}

.side-menu {
  border-right: none;
}

:deep(.side-menu .el-menu-item) {
  height: 50px;
  margin: 8px 8px;
  border-radius: 14px;
}

:deep(.side-menu .el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(59, 135, 255, 0.92), rgba(100, 96, 255, 0.92));
  box-shadow: 0 10px 24px rgba(49, 102, 255, 0.28);
}

.layout-main-wrap {
  min-width: 0;
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-radius: 28px;
}

.header-kicker {
  color: #88b3ff;
  font-size: 12px;
  letter-spacing: 0.18em;
}

.header-title {
  margin-top: 8px;
  font-size: 30px;
  font-weight: 700;
  color: #f7fbff;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 14px;
}

.user-pill {
  padding: 10px 16px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(152, 184, 255, 0.14);
}

.user-pill span,
.user-pill strong {
  display: block;
}

.user-pill span {
  color: #f4f8ff;
}

.user-pill strong {
  margin-top: 4px;
  color: #8fb6ff;
  font-size: 12px;
  letter-spacing: 0.12em;
}

.layout-main {
  padding: 18px 0 0;
}

@media (max-width: 960px) {
  .layout-shell {
    padding: 12px;
  }

  .layout-container {
    flex-direction: column;
  }

  .layout-aside {
    width: 100% !important;
  }

  .layout-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 18px;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 640px) {
  .header-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .header-title {
    font-size: 24px;
  }

  .role-card strong {
    font-size: 22px;
  }
}
</style>
