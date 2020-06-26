import Vue from 'vue'
import Router from 'vue-router'
import BatchRouter from './modules/batch'
import Mypage from '@/components/common/Mypage'
import HelloWorld from '@/components/HelloWorld'
import DashBoard from '@/components/common/DashBoard'
import EmptyPage from '@/components/common/EmptyPage'

import Login from '@/components/common/Login'
// 로그인 성공시, actions에서 store에 isAuth값을 true로 바꿔줬다. 그걸 이용한다.
import store from '@/vuex/store'

const requireAuth = () => (from, to, next) => {
  // console.log('router index.js file pass // store.getters.getIsAuth : ' + store.getters.getIsAuth)
  let auth = store.getters.getIsAuth
  if (auth) {
    return next() // isAuth === true면 페이지 이동
  }
  next('/') // isAuth === false면 다시 로그인 화면으로 이동
}
const loginDup = () => (from, to, next) => {
  let auth = store.getters.getIsAuth
  if (auth) {
    // eslint-disable-next-line no-return-assign
    return location = '/main/dashboard'
  }
  next()
}

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [{
    path: '/',
    name: 'login',
    beforeEnter: loginDup(), // 로그인 이후 '/' path 접근 redirect
    component: Login
  },
  {
    path: '/main',
    name: 'helloworld',
    component: HelloWorld,
    redirect: '/main/dashboard',
    beforeEnter: requireAuth(), // HelloWorld에 진입하기 전에 requireAuth 함수를 실행한다
    children: [{
      path: 'dashboard',
      name: 'dashboard',
      component: DashBoard
    },
    {
      path: 'emptypage',
      name: 'emptypage',
      component: EmptyPage,
      props: true// params 를 받기위해서 true 값을 부여
    },
    {
      path: 'mypage',
      name: 'mypage',
      component: Mypage
    },
    BatchRouter
    ]
  }
  ]
})

router.beforeEach((to, from, next) => {
  // 로그인 만료체크
  let path = to.fullPath
  if (path !== '/') {
    store.dispatch('loginExpired').then(result => {
      if (result) {
        next()
      } else {
        next('/')
      }
    })
  } else {
    next()
  }
})

export default router
