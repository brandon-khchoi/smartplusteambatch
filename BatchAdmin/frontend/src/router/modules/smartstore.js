/** When your routing table is too long, you can split it into small modules **/

import SmartStore from '@/components/smartstore/SmartStore'
import CategoryMatchList from '@/components/smartstore/CategoryMatchList'
import CategoryUnMatchNext from '@/components/smartstore/CategoryUnMatchNext'
import ProductList from '@/components/smartstore/product/ProductList'
import ShippingApiList from '@/components/smartstore/shipping/ShippingApiList'
import ShippingApiCreate from '@/components/smartstore/shipping/ShippingApiCreate'
import SellerList from '@/components/smartstore/SellerList'
import SellerCreate from '@/components/smartstore/SellerCreate'
import MallSizeInfoList from '@/components/smartstore/MallSizeInfoList'
import store from '@/vuex/store'

const userMenuAuth = {}
const requireAuth = (currentName) => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let auth = false
  userSubMenuList.forEach(element => {
    // 스마크 스토어 메뉴 no = 6
    if (element.menu_main_no === 6 && element.menu_url_addr === currentName) {
      auth = true
      let menuAuth = element.auth_val
      userMenuAuth['menu_display'] = (menuAuth & 1) === 1
      userMenuAuth['retrieve_list'] = (menuAuth & 2) === 2
      userMenuAuth['retrieve_detail'] = (menuAuth & 4) === 4
      userMenuAuth['create_data'] = (menuAuth & 8) === 8
      userMenuAuth['modify_data'] = (menuAuth & 16) === 16
      userMenuAuth['delete_data'] = (menuAuth & 32) === 32
      userMenuAuth['excl_down'] = (menuAuth & 64) === 64
    }
  })

  if (auth) {
    return next()
  }
}

const SmartStoreRouter = {
  path: '/smartstore',
  component: SmartStore,
  // redirect: '/smartstore/category',
  name: 'smartstore',
  children: [
    {
      path: 'category',
      component: CategoryMatchList,
      beforeEnter: requireAuth('categorymatchlist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'categorymatchlist'
    },
    {
      path: 'category/unmatchnext',
      component: CategoryUnMatchNext,
      beforeEnter: requireAuth('categoryunmatchnext'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'categoryunmatchnext'
    },
    {
      path: 'shippingapi/list',
      component: ShippingApiList,
      beforeEnter: requireAuth('shippingapilist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'shippingapilist'
    },
    {
      path: 'shippingapi/update/:mall_no/:tab_name',
      component: ShippingApiCreate,
      beforeEnter: requireAuth('shippingapilist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'shippingapiupdate'
    },
    {
      path: 'shippingapi/create',
      component: ShippingApiCreate,
      beforeEnter: requireAuth('shippingapilist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'shippingapicreate'
    },
    {
      path: 'seller/list',
      component: SellerList,
      beforeEnter: requireAuth('sellerlist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'sellerlist'
    },
    {
      path: 'seller/update/:mall_id/:mall_no/:store_no',
      component: SellerCreate,
      beforeEnter: requireAuth('sellerlist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'sellerupdate'
    },
    {
      path: 'seller/create',
      component: SellerCreate,
      beforeEnter: requireAuth('sellerlist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'sellercreate'
    },
    {
      path: 'product/list',
      component: ProductList,
      beforeEnter: requireAuth('productlist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'productlist'
    },
    {
      path: 'mallsizeinfolist',
      component: MallSizeInfoList,
      beforeEnter: requireAuth('mallsizeinfolist'),
      props: {'userMenuAuth': userMenuAuth},
      name: 'mallsizeinfolist'
    }
  ]
}

export default SmartStoreRouter
