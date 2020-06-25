<template>
  <div class="ShippingCreateReturn">
    <div class="tableWrap tableRegister ">

      <table summary="대표 반품/교환지 설정">
          <caption class="blind">대표 반품/교환지 설정</caption>
          <colgroup>
              <col style="width:200px"/>
              <col style="width:340px"/>
              <col style="width:150px"/>
              <col/>
          </colgroup>
          <tbody>
          <tr>
              <th style="border-top: 0px;">대표 배송지 설정</th>
              <td style="border-top: 0px;" colspan="3">

                  <span class="inputWrap">
                    <template v-if="$route.params.mall_no">
                      <input type="checkbox" id="service01-02" :true-value=1 :false-value=0 v-model="shippingReturn.delivery_return_default_yn" disabled>
                      <label for="service01-02">대표 반품/교환지로 설정</label>
                    </template>
                    <template v-else>
                      <input type="checkbox" id="service01-02" :true-value=1 :false-value=0 v-model="shippingReturn.delivery_return_default_yn" disabled>
                      <label for="service01-02">대표 반품/교환지로 설정</label>
                    </template>
                  </span>
              </td>
          </tr>
          <tr>
              <th>배송지조회<span class="necessary">*</span></th>
              <td colspan="3">
                <button type="button" class="btn_min"
                  @click="getAddressList() " >주소목록 조회
                </button>
                 <button type="button" class="btn_min" style="width:135px;"
                  @click="getReturnsCompanyList() " >반품/교환 택배사 조회
                </button>
              </td>
          </tr>
            <tr>
              <th>배송지명<span class="necessary">*</span></th>
              <td colspan="3">
                <input type="text" v-model="shippingReturn.delivery_name"
                  readonly style="pointer-events: none;background:#f1f1f1;"
                  />
              </td>
          </tr>
          <tr>
              <th>우편번호<span class="necessary">*</span></th>
              <td colspan="3">
                  <div class="addressWrap">
                      <div >
                      <input type="text" v-model="shippingReturn.delivery_zipcode_no"
                        readonly style="pointer-events: none;background:#f1f1f1;"
                        />
                      </div>
                  </div>
              </td>
          </tr>
          <tr>
              <th>주소<span class="necessary">*</span></th>
              <td colspan="3">
                <input type="text" class="noinputBox" v-model="shippingReturn.delivery_addr" readonly
                  style="pointer-events: none;background:#f1f1f1;"
                />
              </td>
          </tr>
          <tr>
              <th>주소 상세<span class="necessary">*</span></th>
              <td colspan="3">
                  <input type="text"
                    v-model="shippingReturn.delivery_detail_addr"
                    style="pointer-events: none;background:#f1f1f1;"
                    readonly
                    maxlength="100"
                  />
              </td>
          </tr>
          <tr>
              <th>전화번호<span class="necessary">*</span></th>
              <td colspan="3">
                <input type="text"
                  v-model ="shippingReturn.delivery_tel_no"
                  maxlength="15"
                  style="pointer-events: none;background:#f1f1f1;"
                  @input="$parent.typing($event, 'no', shippingReturn,'tel')"
                />
              </td>
          </tr>
          <tr>
            <th>반품배송비(편도)<span class="necessary">*</span></th>
            <td colspan="3">
              <p class="textWordWrap">
                <input type="number" v-model.number="shippingReturn.delivery_return_price"
                  style="display: none;" />
                <input type="text" :value="shippingReturn.delivery_return_price | currency" style="text-align:right;"
                  @input="$parent.addComma( shippingReturn, 'delivery_return_price')" maxlength="10" />
                원
              </p>
            </td>
          </tr>
          <tr>
            <th>교환배송비(왕복)<span class="necessary">*</span></th>
            <td colspan="3">
                <p class="textWordWrap">
                  <input type="number" v-model.number="shippingReturn.delivery_exchange_price"
                  style="display: none;" />
                  <input type="text" :value="shippingReturn.delivery_exchange_price | currency" style="text-align:right;"
                  @input="$parent.addComma( shippingReturn, 'delivery_exchange_price')" maxlength="10" />
                  원
              </p>
            </td>
          </tr>
          </tbody>
      </table>

    </div>

    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalAddressList from './ModalAddressList'
import ModalReturnsCompanyList from './ModalReturnsCompanyList'
import http from '@/http-commons'

import { eventBus } from '@/main'

let shippingInitData = {
  delivery_return_default_yn: 1, // 대표 반품/교환지 설정
  delivery_name: '', // 배송지명
  delivery_zipcode_no: '', // 우편번호
  delivery_addr: '', // 주소
  delivery_detail_addr: '', // 주소 상세
  delivery_tel_no: '', // 전화번호1
  delivery_store_addr_no: '', // 스토어 주소 번호
  delivery_return_price: 0, // 반품배송비(편도)
  delivery_exchange_price: 0 // 교환배송비(왕복)
}

export default {
  name: 'ShippingApiCreateReturn',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      showTabName: '',
      shippingReturn: JSON.parse(JSON.stringify(shippingInitData)),
      valiAlert: {
        delivery_name: '배송지명을 입력해주세요.',
        delivery_zipcode_no: '우편번호를 입력해주세요.',
        delivery_addr: '주소를 입력해주세요.',
        delivery_detail_addr: '상세주소를 입력해주세요.',
        delivery_tel_no: '전화번호를 입력해주세요.',
        delivery_return_price: '반품배송비(편도)를 입력해주세요.',
        delivery_exchange_price: '교환배송비(왕복)를 입력해주세요'
      }
    }
  },
  created () {
  },
  beforeDestroy () {
    eventBus.$off('modalAddressListReturn')
    eventBus.$off('commonServiceListChange')
    eventBus.$off('ReturnUpdate')
    eventBus.$off('getReturn')
  },
  mounted () {
    // 수정시 탭 대표 반품/교환지 데이터
    eventBus.$on('getReturn', (updateReturn) => {
      // 검색버튼 및 탭클릭으로 호출 됨
      console.log('탭 검샙 반품지')
      this.shippingReturn = updateReturn
      // this.get(storeDeliveryNo)
    })
    eventBus.$on('ReturnUpdate', (name) => {
      this.save(name)
    })

    /** 스마트 스토어 주소지 선택 */
    eventBus.$on('modalAddressListReturn', (item) => {
      this.matchAddress(item)
    })

    /** 공통 select box 변경시 init 상태로 data 변경 */
    eventBus.$on('commonServiceListChange', () => {
      this.shippingReturn = JSON.parse(JSON.stringify(shippingInitData))
    })
  },
  methods: {
    matchAddress (item) {
      this.shippingReturn.delivery_name = item.name
      this.shippingReturn.delivery_detail_addr = item.detailAddress
      this.shippingReturn.delivery_addr = item.baseAddress
      this.shippingReturn.delivery_tel_no = item.phoneNumber1
      this.shippingReturn.delivery_zipcode_no = item.postalCode
      this.shippingReturn.delivery_store_addr_no = item.addressId
      this.$forceUpdate()
    },
    save (name) {
      if (!this.validationCheck()) return false

      let mallNo = this.$store.state.searchParam.mall_no
      let form = new FormData()
      form.append('mall_no', mallNo)
      form.append('delivery_name', this.shippingReturn.delivery_name)
      form.append('delivery_tel_no', this.shippingReturn.delivery_tel_no)
      form.append('delivery_zipcode_no', this.shippingReturn.delivery_zipcode_no)
      form.append('delivery_addr', this.shippingReturn.delivery_addr)
      form.append('delivery_detail_addr', this.shippingReturn.delivery_detail_addr)
      form.append('delivery_return_price', this.shippingReturn.delivery_return_price)
      form.append('delivery_exchange_price', this.shippingReturn.delivery_exchange_price)

      form.append('delivery_return_default_yn', this.shippingReturn.delivery_return_default_yn)
      form.append('delivery_store_addr_no', this.shippingReturn.delivery_store_addr_no)

      form.append('user_code', 1)
      form.append('user_no', this.userNo)

      this.isLoading = true

      let url = ''
      form.append('store_delivery_no', this.shippingReturn.store_delivery_no)
      url = '/smartstore/shipping/api/updatereturn'

      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          if (response.data.data === 1) {
            this.$modal.show(ModalAlert,
              {title: '', text: '대표 반품/교환지 정보를 수정하였습니다.'},
              { width: 450, height: 'auto' }
            )
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
          if (e.response.data.responseCode === -3) {
            this.$modal.show(ModalAlert,
              { title: '', text: e.response.data.data }, { width: 450, height: 'auto' }
            )
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
            )
          }
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    validationCheck () {
      let isCheck = true
      for (let item in this.valiAlert) {
        if (!this.shippingReturn[item]) {
          this.$modal.show(ModalAlert,
            { title: '대표 반품/교환지 설정', text: this.valiAlert[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return isCheck
    },
    getAddressList () {
      const mallId = this.$store.state.searchParam.mall_id
      if (mallId === '0') {
        this.$modal.show(ModalAlert,
          { title: '', text: '자사몰명을 선택해주세요.' }, { width: 450, height: 'auto' }
        )
        return
      }

      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/shipping/addresslist/' + mallId
      this.isLoading = true
      http
        .get(url)
        .then(response => {
          this.isLoading = false
          let list = response.data.addressBookList.addressBook

          this.$modal.show(ModalAddressList,
            { list: list, userMenuAuth: this.userMenuAuth, whereOpen: 'ShippingApiCreateReturn' },
            { width: '50%', height: 'auto', clickToClose: false, scrollable: true }
          )
        })
        .catch(e => {
          // console.log(e)
          this.isLoading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    getReturnsCompanyList () {
      const mallId = this.$store.state.searchParam.mall_id
      if (mallId === '0') {
        this.$modal.show(ModalAlert,
          { title: '', text: '자사몰명을 선택해주세요.' }, { width: 450, height: 'auto' }
        )
        return
      }

      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/shipping/returncompanylist/' + mallId
      this.isLoading = true
      http
        .get(url)
        .then(response => {
          this.isLoading = false
          let list = response.data.returnsCompanyList.returnsCompany

          this.$modal.show(ModalReturnsCompanyList,
            { list: list, userMenuAuth: this.userMenuAuth },
            { width: '30%', height: 'auto', clickToClose: false, scrollable: true }
          )
        })
        .catch(e => {
        // console.log(e)
          this.isLoading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    }
  },
  props: {
    'userMenuAuth': Object
  }
}
</script>

<style scoped>
.tab.tab2 td {
    padding: 10px 10px;
}
</style>
