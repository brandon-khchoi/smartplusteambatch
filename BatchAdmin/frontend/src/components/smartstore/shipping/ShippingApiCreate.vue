<template>
  <div class="ShippingCreate">
    <div class="content">

      <template v-if="$route.params.mall_no">
        <h1>배송지 수정</h1>
      </template>
      <template v-else>
        <h1>신규 배송지 등록</h1>
      </template>
      <div class="formContainer">
        <article>
          <div class="articleWrap">

            <div class="tabContainer">
              <ul class="tabBtnWrap">
                  <li class="tabBtn" :class="{ on : isShowTab }" id="tab1" @click="changeTab('ShippingApiCreateRelease')" >대표 출고지 설정</li>
                  <li class="tabBtn" :class="{ on : !isShowTab }" id="tabSecond" @click="changeTab('ShippingApiCreateReturn') " >대표 반품/교환지 설정</li>
              </ul>
              <CommonServiceList :propMallNo="$route.params.mall_no"></CommonServiceList>
              <ul class="tabContents">
                <li class="tab tab1" style="position: relative;">
                  <ShippingApiCreateRelease
                    v-show="showTabName === 'ShippingApiCreateRelease'"
                    :userMenuAuth=userMenuAuth
                  >
                  </ShippingApiCreateRelease>
                </li>
                <li class="tab tabSecond" style="position: relative; display: list-item;">
                  <ShippingApiCreateReturn
                    v-show="showTabName === 'ShippingApiCreateReturn'"
                    :userMenuAuth=userMenuAuth
                  >
                  </ShippingApiCreateReturn>
                </li>

              </ul>
            </div>
          </div>
        </article>
        <div class="btnWrap btnEnd" style="text-align:center;">
          <ul>
            <li>
              <input class="btn_cancel btn-lg" type="button" value="목록" @click="backBtn()"/>
            </li>
            <li>
              <template v-if="$route.params.mall_no">
                <template v-if="showTabName === 'ShippingApiCreateRelease'">
                  <input v-if="userMenuAuth.modify_data" class="btn_save btn-lg" type="button" value="대표출고지수정" @click="saveChild('update')"/>
                </template>
                <template v-if="showTabName === 'ShippingApiCreateReturn'">
                  <input v-if="userMenuAuth.modify_data" class="btn_save btn-lg" type="button" value="대표 반품/교환지 수정" @click="saveChild('update')"/>
                </template>
              </template>
              <template v-else>
                <input v-if="userMenuAuth.create_data" class="btn_save btn-lg" type="button" value="등록" @click="saveChild('create')"/>
              </template>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert'
import CommonServiceList from '../CommonServiceList'
// import ModalBundleList from '../ModalBundleList'
// import ModalAddressList from '../ModalAddressList'
import ShippingApiCreateRelease from './ShippingApiCreateRelease'
import ShippingApiCreateReturn from './ShippingApiCreateReturn'

import http from '@/http-commons'
import { eventBus } from '@/main'

export default {
  name: 'ShippingApiCreate',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      showTabName: '',
      isShowTab: true,
      totShippingData: {
        shippingRelease: {

        },
        shippingReturn: {

        }
      }
    }
  },
  mounted () {
    if (this.$route.params.mall_no && this.$route.params.tab_name) { // update 로 들어온 경우
      this.get(this.$route.params.mall_no)

      if (this.$route.params.tab_name === 'release') {
        console.log('릴리즈 ')
        this.showTabName = 'ShippingApiCreateRelease'
        this.isShowTab = true
        // eventBus.$emit('getRelease', this.$route.params.store_delivery_no)
      } else if (this.$route.params.tab_name === 'return') {
        console.log('리턴 ')
        this.showTabName = 'ShippingApiCreateReturn'
        this.isShowTab = false
        // eventBus.$emit('getReturn', this.$route.params.store_delivery_no)
      }
    } else {
      this.showTabName = 'ShippingApiCreateRelease'
    }
  },
  methods: {
    typing: function (event, type, item, name) {
      let patternNo = /^[0-9]+$/
      let patternTel = /^[0-9]+$/
      let message = event.target.value.trim()
      if (type === 'no') {
        if (patternNo.test(message) === false) {
          item[name] = ''
          event.target.value = ''
        } else {
          item[name] = parseInt(message)
          event.target.value = parseInt(message)
        }
      } else if (type === 'tel') {
        if (patternTel.test(message) === false) {
          item[name] = ''
          event.target.value = ''
        } else {
          item[name] = parseInt(message)
          event.target.value = parseInt(message)
        }
      }
    },
    get (mallNo) {
      let url = '/smartstore/shipping/api/infomall/' + mallNo
      http
        .get(url)
        .then(response => {
          // this.shipping = response.data.data.release
          eventBus.$emit('getRelease', response.data.data.release)
          eventBus.$emit('getReturn', response.data.data.return)
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
          this.backBtn()
        })
    },
    backBtn () {
      if (this.$route.params.mall_no) { // update 로 들어온 경우
        this.$router.push({name: 'shippingapilist', query: { mall_no: this.$store.state.searchParam.mall_no }})
      } else {
        this.$router.push({name: 'shippingapilist'})
      }
    },
    addComma (item, part) {
      let strNumber = event.target.value.replace(/,/g, '') // 컴마뺀 string 숫자
      let patternNo = /^[0-9]+$/
      // let patternNo = /[^0-9]/g
      if (patternNo.test(strNumber) === false) { // 숫자 아닐경우 input value 0
        event.target.value = 0
        item[part] = 0
      } else {
        item[part] = parseInt(strNumber)
      }
    },
    changeTab (tabName) {
      this.showTabName = tabName
    },
    saveChild (name) {
      if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
        this.$modal.show(ModalAlert,
          {title: '', text: '쇼핑몰을 선택해주세요.'},
          { width: 450, height: 'auto' }
        )
        return
      }
      if (name === 'create') {
        console.log('등록')
        // let context = this

        for (let child of this.$children) {
          if (child.$options.name === 'ShippingApiCreateRelease') {
            this.totShippingData.shippingRelease = child.shippingRelease
            if (child.shippingRelease.bundle_check === 1) { // 묶음배송 사용
              this.totShippingData.shippingRelease.delivery_bundle_group_code = child.shippingRelease.delivery_bundle_group_code
              if (child.shippingRelease.delivery_special_2_zone_price_bundle !== 0 && child.delivery_special_2_zone_price_check_bundle === true) {
                this.totShippingData.shippingRelease.delivery_special_2_zone_price = child.shippingRelease.delivery_special_2_zone_price_bundle
              }
              if (child.shippingRelease.delivery_special_2_zone_price_bundle !== 0 && child.shippingRelease.delivery_special_3_zone_price_bundle !== 0 &&
                  child.delivery_special_2_zone_price_check_bundle === true && child.delivery_special_3_zone_price_check_bundle === true) {
                this.totShippingData.shippingRelease.delivery_special_3_zone_price = child.shippingRelease.delivery_special_3_zone_price_bundle
              }
              if (child.shippingRelease.delivery_bundle_cal_type_code) {
                this.totShippingData.shippingRelease.delivery_bundle_cal_type_code = child.shippingRelease.delivery_bundle_cal_type_code
              }
            } else { // 묶음배송 미사용
              if (child.shippingRelease.delivery_special_2_zone_price !== 0 && child.delivery_special_2_zone_price_check === true) {
                this.totShippingData.shippingRelease.delivery_special_2_zone_price = child.shippingRelease.delivery_special_2_zone_price
              }
              if (child.shippingRelease.delivery_special_2_zone_price !== 0 && child.shippingRelease.delivery_special_3_zone_price !== 0 &&
                   child.delivery_special_2_zone_price_check === true && child.delivery_special_3_zone_price_check === true) {
                this.totShippingData.shippingRelease.delivery_special_3_zone_price = child.shippingRelease.delivery_special_3_zone_price
              }
            }
            if (!child.validationCheck()) {
              return
            }
          }
          if (child.$options.name === 'ShippingApiCreateReturn') {
            this.totShippingData.shippingReturn = child.shippingReturn
            if (!child.validationCheck()) {
              return
            }
          }
        }

        this.insertData()
      } else if (name === 'update') {
        if (this.showTabName === 'ShippingApiCreateRelease') {
          eventBus.$emit('ReleaseUpdate', name)
        } else {
          eventBus.$emit('ReturnUpdate', name)
        }
      }
    },
    insertData () {
      this.totShippingData.mall_no = this.$store.state.searchParam.mall_no
      this.totShippingData.user_no = this.userNo
      this.totShippingData.user_code = 1
      let url = '/smartstore/shipping/api/createmultiple'
      let params = this.totShippingData
      this.isLoading = true
      http
        .post(url, JSON.stringify(params))
        .then(response => {
          this.isLoading = false
          if (response.data.data === 1) {
            this.$modal.show(ModalAlert,
              {title: '', text: '신규 배송지가 등록되었습니다.'},
              { width: 450, height: 'auto' }
            )
            this.$router.push({ name: 'shippingapilist', query: { mall_no: this.$store.state.searchParam.mall_no } })
          } else if (response.data.data === 0) {
            this.$modal.show(ModalAlert,
              {title: '', text: '데이터 업로드에 실패하였습니다.'},
              { width: 450, height: 'auto' }
            )
          } else {
            this.$modal.show(ModalAlert,
              {title: '', text: response.data.data},
              { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          console.log(e)
        })
        .finally(() => {
          this.isLoading = false
        })
    }
  },
  components: {
    CommonServiceList,
    ShippingApiCreateRelease,
    ShippingApiCreateReturn
  },
  props: {
    'userMenuAuth': Object
  }
}
</script>

<style scoped>

</style>
