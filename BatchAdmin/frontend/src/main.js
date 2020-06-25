// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VModal from 'vue-js-modal'
import store from './vuex/store'
import Spinner from './components/common/Spinner.vue'
import VeeValidate from 'vee-validate'
import commFunc from './common/commFunc'
import validation from './common/validation'
import VueLazyload from 'vue-lazyload'
import BootstrapVue from 'bootstrap-vue'
import VueWaypoint from 'vue-waypoint'
import vSelect from 'vue-select'
import VueJsonPretty from 'vue-json-pretty'

Vue.use(BootstrapVue)

import('./styles/index.css')

export const eventBus = new Vue()

Vue.config.productionTip = false

Vue.component('v-select', vSelect)
Vue.component('Spinner', Spinner)
Vue.component('VueJsonPretty', VueJsonPretty)
Vue.use(VueWaypoint)
Vue.use(VeeValidate, {fieldsBagName: 'formFields'})
Vue.use(VModal, { dynamic: true, injectModalsContainer: true, dialog: true })
Vue.use(VueLazyload, {
  preLoad: 1.5
})
Vue.filter('currency', function (value) {
  if (value === '') return ''

  return parseInt(value).toLocaleString() // EX) 1500 -> 1,500
})
/* 조합형 필수 옵션일 경우 구분자 변경 */
Vue.filter('removeSparator', function (value) {
  if (value === '') return ''

  return value.replace('$$', '+')
})
const cf = commFunc
const valid = validation

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  mixins: [cf, valid],
  components: { App },
  template: '<App/>'
})
