<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import api from '../utils/api'

const router = useRouter()
const userStore = useUserStore()

const isLogin = ref(true)
const loginForm = ref({ username: '', password: '' })
const registerForm = ref({ username: '', password: '', realName: '', role: 'STUDENT', email: '', phone: '' })
const demoAccounts = [
  { role: '管理员', username: 'admin', password: 'admin123' },
  { role: '教师', username: 'teacher1', password: 'teacher123' },
  { role: '学生', username: 'student1', password: 'student123' }
]

const handleLogin = async () => {
  try {
    const res = await api.post('/auth/login', loginForm.value)
    userStore.setLogin(res.data.token, res.data)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (e) { /* handled by interceptor */ }
}

const handleRegister = async () => {
  try {
    await api.post('/auth/register', registerForm.value)
    ElMessage.success('注册成功，请登录')
    isLogin.value = true
  } catch (e) { /* handled by interceptor */ }
}
</script>

<template>
  <div class="login-container app-shell">
    <div class="login-grid page-section">
      <section class="login-hero">
        <span class="hero-kicker">SMART CAMPUS / ENROLLMENT HUB</span>
        <h1>把选课系统做得像产品，而不是表格。</h1>
        <p>
          课程检索、容量控制、身份分权、教师授课视图与管理员后台，全部收拢到一套更有张力的界面里。
        </p>
        <div class="hero-chips">
          <span class="data-chip">实时选课状态</span>
          <span class="data-chip">JWT 身份鉴权</span>
          <span class="data-chip">教师 / 学生 / 管理员</span>
        </div>
        <div class="hero-panel glass-panel">
          <div class="hero-panel-head">
            <span>演示账号</span>
            <span>点击即可填充</span>
          </div>
          <div class="account-list">
            <button
              v-for="item in demoAccounts"
              :key="item.role"
              class="account-card"
              type="button"
              @click="loginForm = { username: item.username, password: item.password }"
            >
              <strong>{{ item.role }}</strong>
              <span>{{ item.username }}</span>
              <em>{{ item.password }}</em>
            </button>
          </div>
        </div>
      </section>

      <section class="login-card glass-panel">
        <div class="login-card-head">
          <span class="login-badge">选课中枢</span>
          <h2>学生选课系统</h2>
          <p>更锐利的视觉，更直接的操作流。</p>
        </div>

        <el-tabs v-model="isLogin" class="login-tabs stretch-tabs">
          <el-tab-pane label="登录" :name="true">
            <el-form @submit.prevent="handleLogin" label-width="0" class="auth-form">
              <el-form-item>
                <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" size="large" />
              </el-form-item>
              <el-form-item>
                <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleLogin" size="large" class="submit-button">立即进入系统</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="注册" :name="false">
            <el-form @submit.prevent="handleRegister" label-width="0" class="auth-form">
              <el-form-item>
                <el-input v-model="registerForm.username" placeholder="用户名" prefix-icon="User" size="large" />
              </el-form-item>
              <el-form-item>
                <el-input v-model="registerForm.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
              </el-form-item>
              <el-form-item>
                <el-input v-model="registerForm.realName" placeholder="真实姓名" prefix-icon="UserFilled" size="large" />
              </el-form-item>
              <el-form-item>
                <el-select v-model="registerForm.role" placeholder="选择角色" size="large" style="width: 100%">
                  <el-option label="学生" value="STUDENT" />
                  <el-option label="教师" value="TEACHER" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input v-model="registerForm.email" placeholder="邮箱（选填）" prefix-icon="Message" size="large" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleRegister" size="large" class="submit-button">创建账号</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </section>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  min-height: 100vh;
  padding: 40px;
}

.login-grid {
  width: min(1220px, 100%);
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1.15fr 0.85fr;
  gap: 28px;
  align-items: stretch;
}

.login-hero {
  padding: 56px 18px 56px 8px;
}

.hero-kicker {
  display: inline-block;
  margin-bottom: 18px;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(164, 191, 255, 0.16);
  color: #9fc9ff;
  font-size: 12px;
  letter-spacing: 0.18em;
}

.login-hero h1 {
  max-width: 620px;
  font-size: 64px;
  line-height: 1.02;
  letter-spacing: -0.04em;
  color: #f7fbff;
}

.login-hero p {
  max-width: 560px;
  margin-top: 24px;
  font-size: 18px;
  line-height: 1.8;
  color: rgba(227, 236, 255, 0.74);
}

.hero-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 28px;
}

.hero-panel {
  margin-top: 42px;
  padding: 24px;
  border-radius: 28px;
}

.hero-panel-head {
  display: flex;
  justify-content: space-between;
  margin-bottom: 18px;
  color: rgba(233, 241, 255, 0.8);
  font-size: 14px;
}

.account-list {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
}

.account-card {
  padding: 18px;
  border: 1px solid rgba(139, 173, 255, 0.14);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.04);
  color: #eff5ff;
  text-align: left;
  cursor: pointer;
  transition: transform 0.25s ease, border-color 0.25s ease, background 0.25s ease;
}

.account-card:hover {
  transform: translateY(-4px);
  border-color: rgba(107, 181, 255, 0.4);
  background: rgba(84, 136, 255, 0.08);
}

.account-card strong,
.account-card span,
.account-card em {
  display: block;
}

.account-card strong {
  font-size: 16px;
}

.account-card span {
  margin-top: 12px;
  color: #90b4ff;
}

.account-card em {
  margin-top: 8px;
  color: rgba(232, 238, 252, 0.72);
  font-style: normal;
}

.login-card {
  padding: 32px;
  border-radius: 32px;
  align-self: center;
}

.login-card-head {
  margin-bottom: 24px;
}

.login-badge {
  display: inline-flex;
  padding: 7px 12px;
  border-radius: 999px;
  background: rgba(74, 129, 255, 0.12);
  color: #9ecbff;
  font-size: 12px;
  letter-spacing: 0.16em;
}

.login-card-head h2 {
  margin-top: 18px;
  font-size: 34px;
  color: #f7fbff;
}

.login-card-head p {
  margin-top: 10px;
  color: rgba(220, 230, 255, 0.72);
}

.auth-form {
  margin-top: 16px;
}

.submit-button {
  width: 100%;
  height: 50px;
  border-radius: 14px;
  font-weight: 700;
  letter-spacing: 0.06em;
}

:deep(.stretch-tabs .el-tabs__header) {
  margin-bottom: 26px;
}

:deep(.stretch-tabs .el-tabs__nav-wrap::after) {
  background-color: rgba(150, 176, 240, 0.12);
}

:deep(.stretch-tabs .el-tabs__active-bar) {
  background: linear-gradient(90deg, #31b1ff, #7464ff);
  height: 3px;
}

@media (max-width: 1080px) {
  .login-grid {
    grid-template-columns: 1fr;
  }

  .login-hero {
    padding: 12px 0 0;
  }

  .login-hero h1 {
    font-size: 44px;
  }

  .account-list {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .login-container {
    padding: 20px;
  }

  .login-card {
    padding: 22px;
    border-radius: 24px;
  }

  .login-hero h1 {
    font-size: 34px;
  }
}
</style>
