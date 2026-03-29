import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'courses',
        name: 'CourseList',
        component: () => import('../views/CourseList.vue'),
        meta: { title: '课程列表' }
      },
      {
        path: 'my-courses',
        name: 'MyCourses',
        component: () => import('../views/MyCourses.vue'),
        meta: { title: '我的选课', role: 'STUDENT' }
      },
      {
        path: 'course-manage',
        name: 'CourseManage',
        component: () => import('../views/CourseManage.vue'),
        meta: { title: '课程管理', role: 'ADMIN' }
      },
      {
        path: 'user-manage',
        name: 'UserManage',
        component: () => import('../views/UserManage.vue'),
        meta: { title: '用户管理', role: 'ADMIN' }
      },
      {
        path: 'teacher-courses',
        name: 'TeacherCourses',
        component: () => import('../views/TeacherCourses.vue'),
        meta: { title: '我的授课', role: 'TEACHER' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
