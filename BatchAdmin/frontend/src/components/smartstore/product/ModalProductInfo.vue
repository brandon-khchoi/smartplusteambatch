<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          상품명 : {{ item.product_name }}
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="popupContentWrap">
            <div class="popupContent">
                <div class="searchBtnWrap" style="text-align:left; margin-left: 10px;">
                  <input type="text" v-model="modalItem.product_name" @keyup.enter="get(modalItem)"
                    @input="typingProductName(modalItem.product_name)"
                    placeholder="상품명">
                  <input type="text" v-model="product_no" @keyup.enter="get(modalItem)"
                    @input="typingProductId(modalItem.product_name)"
                    placeholder="상품ID">
                  <p class="btn_search" @click="get(modalItem)">찾기</p>
                </div>
                <!-- :style="[item.changed ? { 'background': '#fff0f0'} : {}]" -->
                <div class="tableWrap tableList tableHover"
                    :style="[ modalList.length === 0 ? { 'overflow-y':'auto', 'height': 'auto' } : { 'overflow-y':'scroll', 'height': '600px' } ]" >
                    <table>
                        <colgroup>
                          <col style="width:15%"/>
                          <col style="width:18%"/>
                          <col style="width:22%"/>
                          <col style="width:10%"/>
                          <col style="width:10%"/>
                          <col style="width:15%"/>
                          <col style="width:10%"/>
                        </colgroup>
                        <thead>
                          <th>이미지</th>
                          <th>상품번호</th>
                          <th>상품명</th>
                          <th>판매가</th>
                          <th>할인가</th>
                          <th>판매여부<br>(재고상태)</th>
                          <th>매칭</th>
                        </thead>
                        <tbody>
                          <template v-if="modalList.length > 0">
                          <template v-for="item in modalList">
                            <tr :key = "item.id">
                                <td class="text-center">
                                  <img v-if="item.file_image_detail_text" :src="item.file_image_detail_text"
                                  style="width: 50px;"/>
                                </td>
                                <td class="text-center">{{item.product_no}}</td>
                                <td class="text-center">{{item.product_name}}</td>
                                <td class="text-center">{{item.product_price | currency}}</td>
                                <td class="text-center">{{item.product_pc_discount_price | currency }}</td>
                                <td class="text-center">
                                  <template v-if="item.display_yn === '1'">
                                    판매중
                                  </template>
                                  <template v-else>
                                    미판매
                                  </template>
                                  <template v-if="item.soldout_yn === '1'">
                                    <br>(재고없음)
                                  </template>
                                </td>
                                <td class="textCenter">
                                  <button type="button" class="btn_min" :id="item.product_no + '_btn_match'" @click="matchConfirmDialog(item)">확인</button>
                                </td>
                            </tr>
                          </template>
                          </template>
                          <tr v-else>
                            <td colspan="7" class="emptyTable">
                              해당하는 내용이 없습니다.
                            </td>
                          </tr>
                        </tbody>
                    </table>
                </div>
            </div>
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
    <v-dialog :clickToClose=false />
  </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '@/main'
import http from '@/http-commons'

export default {
  name: 'ModalProductInfo',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      product_no: '',
      modalList: [],
      modalItem: {}
    }
  },
  mounted () {
    this.modalList = JSON.parse(JSON.stringify(this.list))
    this.modalItem = JSON.parse(JSON.stringify(this.item))
  },
  methods: {
    get (item) {
      if (this.product_no === '') {
        if (item.product_name.trim().length < 2) {
          this.$modal.show(ModalAlert,
            { title: '', text: '한글자 이상으로 단어를 검색해주세요.' }, { width: 450, height: 'auto' }
          )
          return
        }
      }

      this.isLoading = true
      let url = '/smartstore/productmatch/unmatched/productinfo'
      http
        .get(url, {
          params: { // query string
            mall_no: this.$store.state.searchParam.mall_no,
            mall_id: this.$store.state.searchParam.mall_id,
            product_name: encodeURIComponent(item.product_name.trim()),
            product_no: this.product_no,
            product_price: item.product_price,
            product_pc_discount_price: item.product_pc_discount_price
          }
        })
        .then(response => {
          this.isLoading = false
          let list = response.data.data

          this.modalList = list
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    typingProductName (name) {
      if (name !== '') {
        this.product_no = ''
      }
    },
    typingProductId (productId) {
      if (productId !== '') {
        this.modalItem.product_name = ''
      }
    },
    /**
     * 상품 매칭 확인 Dialog
     */
    matchConfirmDialog (item) {
      if (!this.userMenuAuth.create_data) {
        return
      }
      this.$modal.show('dialog', {
        title: '상품연동',
        text: '[' + item.product_name + ']으로 상품을 매칭 하시겠습니까?',
        buttons: [{
          title: '취소',
          handler: () => {
            this.$modal.hide('dialog')
          }
        },
        {
          title: '확인', // Button title
          default: true, // ENTER KEY
          handler: () => {
            this.matchProduct(item)
            this.$modal.hide('dialog')
          }
        }]
      })
    },
    /**
     * 상품 매칭 update
     */
    matchProduct (item) {
      this.isLoading = true
      let url = '/smartstore/productmatch/unmatched/match'
      let form = new FormData()
      form.append('mall_no', this.$store.state.searchParam.mall_no)
      form.append('mall_id', this.$store.state.searchParam.mall_id)
      form.append('product_no', item.product_no)
      form.append('smartstore_product_no', this.modalItem.smartstore_product_no)
      form.append('user_no', this.userNo)
      form.append('user_code', 1)
      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          console.log('close')
          if (response.data.data === 0) {
            this.$modal.show(ModalAlert,
              { title: '', text: '옵션데이터를 업데이트 하지 못했습니다.' }, { width: 450, height: 'auto' }
            )
          } else if (response.data.data === 1) {
            this.$modal.show(ModalAlert,
              { title: '', text: '상품 매칭을 완료하였습니다.' }, { width: 450, height: 'auto' }
            )
          } else if (response.data.data === -1) {
            this.$modal.show(ModalAlert,
              { title: '', text: '이미 매칭된 상품입니다.' }, { width: 450, height: 'auto' }
            )
          }
          eventBus.$emit('modalProductInfoUpdate')
          this.$emit('close')
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
          this.$emit('close')
        })
    }
  },
  props: {
    'item': Object,
    'list': Array,
    'userMenuAuth': Object
  }
}
</script>
