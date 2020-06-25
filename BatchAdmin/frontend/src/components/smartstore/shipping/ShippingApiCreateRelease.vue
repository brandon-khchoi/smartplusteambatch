<template>
  <div class="ShippingCreateRelease">
    <div class="tableWrap tableRegister ">

      <table summary="대표 출고지 설정">
          <caption class="blind">대표 출고지 설정</caption>
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
                    <input type="checkbox" id="service01-01" :true-value=1 :false-value=0 v-model="shippingRelease.delivery_release_default_yn" disabled>
                    <label for="service01-01">대표 출고지로 설정</label>
                  </template>
                  <template v-else>
                    <input type="checkbox" id="service01-01" :true-value=1 :false-value=0 v-model="shippingRelease.delivery_release_default_yn" disabled>
                    <label for="service01-01">대표 출고지로 설정</label>
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
              </td>
          </tr>
            <tr>
              <th>배송지명<span class="necessary">*</span></th>
              <td colspan="3">
                <input type="text" v-model="shippingRelease.delivery_name"
                  readonly style="pointer-events: none;background:#f1f1f1;"
                  />
              </td>
          </tr>
          <tr>
              <th>우편번호<span class="necessary">*</span></th>
              <td colspan="3">
                  <div class="addressWrap">
                      <div >
                      <input type="text" v-model="shippingRelease.delivery_zipcode_no"
                        readonly style="pointer-events: none;background:#f1f1f1;"
                        />
                      </div>
                  </div>
              </td>
          </tr>
          <tr>
              <th>주소<span class="necessary">*</span></th>
              <td colspan="3">
                <input type="text" class="noinputBox" v-model="shippingRelease.delivery_addr" readonly
                  style="pointer-events: none;background:#f1f1f1;"
                />
              </td>
          </tr>
          <tr>
              <th>주소 상세<span class="necessary">*</span></th>
              <td colspan="3">
                  <input type="text"
                    v-model="shippingRelease.delivery_detail_addr"
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
                  v-model ="shippingRelease.delivery_tel_no"
                  maxlength="15"
                  style="pointer-events: none;background:#f1f1f1;"
                  @input="$parent.typing($event, 'no', shippingRelease,'tel')"
                />
              </td>
          </tr>
          <tr>
              <th>무료 배송 기준<span class="necessary">*</span></th>
              <td colspan="3">
                  <p class="textWordWrap">
                  <input type="number" v-model.number="shippingRelease.delivery_free_price"
                    style="display: none;" />
                  <input type="text" :value="shippingRelease.delivery_free_price | currency" style="text-align:right;"
                    @input="$parent.addComma( shippingRelease, 'delivery_free_price')" maxlength="10"
                    />
                  원
                </p>
              </td>
          </tr>
          <tr>
              <th>기본배송비<span class="necessary">*</span></th>
              <td colspan="3">
                  <p class="textWordWrap"
                    style="display:inline-block"
                    :style="[delivery_default_price_disable === true ? { 'pointer-events': 'none', 'background' : '#f1f1f1'} : {}]"
                    >
                  <input type="number" v-model.number="shippingRelease.delivery_default_price"
                    style="display: none;" />
                  <input type="text" :value="shippingRelease.delivery_default_price | currency" style="text-align:right;"
                    :disabled="delivery_default_price_disable"
                    :style="[delivery_default_price_disable === true ? { 'pointer-events': 'none', 'background' : '#f1f1f1'} : {}]"
                    @input="$parent.addComma( shippingRelease, 'delivery_default_price')" maxlength="10" />
                  원
                </p>
                  <span v-if="delivery_default_price_disable" class="info red">무료 배송 기준이 2500원 이상인 경우에 등록 가능합니다.</span>
              </td>
          </tr>
          <tr>
              <th>묶음배송비 계산 방식<span class="necessary">*</span></th>
              <td colspan="3">
                <div class="inputContainer">
                  <span class="inputWrap">
                    <input type="radio"  id="service03-01" :value=1 v-model="shippingRelease.bundle_check">
                    <label for="service03-01">묶음배송사용</label>
                  </span>
                  <span class="inputWrap">
                    <input type="radio" id="service03-02" :value=0 v-model="shippingRelease.bundle_check" >
                    <label for="service03-02">묶음배송미사용</label>
                  </span>
                  <transition name="slide-fade">
                    <template v-if="shippingRelease.bundle_check === 1">
                      <div>
                      <template v-if="shippingRelease.delivery_bundle_group_code">
                        선택 [그룹번호 : {{shippingRelease.delivery_bundle_group_code}} ]
                      </template>
                      <button type="button" class="btn_min"
                        @click="getBundleList() " >묶음배송 조회
                      </button>
                      </div>
                    </template>
                  </transition>
                </div>
              </td>
          </tr>
          <transition name="slide-fade">
          <tr v-if="shippingRelease.bundle_check === 1" >
              <th>묶음배송 <br> 도서 산간 추가 배송비</th>
              <td colspan="1" style="border-right:0px;">
                  계산방식 :
                  <template v-if="shippingRelease.delivery_bundle_cal_type_code === 1">
                    최소부과
                  </template>
                  <template v-if="shippingRelease.delivery_bundle_cal_type_code === 2">
                    최대부과
                  </template>
                  <br>
                  <span class="inputWrap">
                    <input type="checkbox"
                      v-model="delivery_special_2_zone_price_check_bundle"
                      id = "delivery_special_2_zone_price_check_bundle"
                      disabled
                      @change="checkAddPrice($event, 'delivery_special_2_zone_price_bundle')">
                    <label for="delivery_special_2_zone_price_check_bundle">2권역(제주 및 도서 산간 지역)</label>
                  </span>

                  <p v-if="delivery_special_2_zone_price_check_bundle"
                    class="textWordWrap" >
                    <input type="number" v-model.number="shippingRelease.delivery_special_2_zone_price_bundle"
                      style="display: none;" />
                    <input type="text" :value="shippingRelease.delivery_special_2_zone_price_bundle | currency" style="text-align:right;"
                      readonly
                      @input="$parent.addComma( shippingRelease, 'delivery_special_2_zone_price_bundle')" maxlength="10" />
                    원
                  </p>
                  <p v-else
                    class="textWordWrap" style="background:#f1f1f1">
                    <input type="number" v-model.number="shippingRelease.delivery_special_2_zone_price_bundle"
                      disabled
                      style="text-align:right;"/>
                    원
                  </p>
              </td>
              <td colspan="2" style="border-left:0px;">
                  <br>
                  <span v-if="delivery_special_2_zone_price_check_bundle" class="inputWrap">
                    <input type="checkbox"
                      v-model="delivery_special_3_zone_price_check_bundle"
                      id = "delivery_special_3_zone_price_check_bundle"
                      disabled
                      @change="checkAddPrice($event, 'delivery_special_3_zone_price_bundle')">
                    <label for="delivery_special_3_zone_price_check_bundle">3권역(제주 외 도서 산간 지역)</label>
                  </span>
                  <span v-else class="inputWrap">
                    <input type="checkbox"
                      v-model="delivery_special_3_zone_price_check_bundle"
                      disabled>
                    <label for="delivery_special_3_zone_price_check_bundle">3권역(제주 외 도서 산간 지역)</label>
                  </span>
                  <p v-if="delivery_special_3_zone_price_check_bundle" class="textWordWrap" >
                    <input type="number" v-model.number="shippingRelease.delivery_special_3_zone_price_bundle"
                      style="display: none;" />
                    <input type="text" :value="shippingRelease.delivery_special_3_zone_price_bundle | currency" style="text-align:right;"
                      readonly
                      @input="$parent.addComma( shippingRelease, 'delivery_special_3_zone_price_bundle')" maxlength="10" />
                    원
                  </p>
                  <p v-else class="textWordWrap" style="background:#f1f1f1">
                  <input type="number" v-model.number="shippingRelease.delivery_special_3_zone_price_bundle"
                    style="text-align:right;"
                    disabled/>
                  원
                  </p>
              </td>
          </tr>
          </transition>
          <transition name="slide-fade">
          <tr v-if="shippingRelease.bundle_check === 0" >
              <th>묶음배송미사용 <br> 도서 산간 추가 배송비</th>
              <td colspan="1" style="border-right:0px;">
                  <span class="inputWrap">
                    <input type="checkbox"
                      v-model="delivery_special_2_zone_price_check"
                      id = "delivery_special_2_zone_price_check"
                      @change="checkAddPrice($event, 'delivery_special_2_zone_price')">
                    <label for="delivery_special_2_zone_price_check">2권역(제주 및 도서 산간 지역)</label>
                  </span>

                  <p v-if="delivery_special_2_zone_price_check"
                    class="textWordWrap" >
                    <input type="number" v-model.number="shippingRelease.delivery_special_2_zone_price"
                      style="display: none;" />
                    <input type="text" :value="shippingRelease.delivery_special_2_zone_price | currency" style="text-align:right;"
                      @input="$parent.addComma( shippingRelease, 'delivery_special_2_zone_price')" maxlength="10" />
                    원
                  </p>
                  <p v-else
                    class="textWordWrap" style="background:#f1f1f1">
                    <input type="number" v-model.number="shippingRelease.delivery_special_2_zone_price"
                      disabled
                      style="text-align:right;"/>
                    원
                  </p>
              </td>
              <td colspan="2" style="border-left:0px;">

                  <span v-if="delivery_special_2_zone_price_check" class="inputWrap">
                    <input type="checkbox"
                      v-model="delivery_special_3_zone_price_check"
                      id = "delivery_special_3_zone_price_check"
                      @change="checkAddPrice($event, 'delivery_special_3_zone_price')">
                    <label for="delivery_special_3_zone_price_check">3권역(제주 외 도서 산간 지역)</label>
                  </span>
                  <span v-else class="inputWrap">
                    <input type="checkbox"
                      v-model="delivery_special_3_zone_price_check"
                      disabled>
                    <label for="delivery_special_3_zone_price_check">3권역(제주 외 도서 산간 지역)</label>
                  </span>
                  <p v-if="delivery_special_3_zone_price_check" class="textWordWrap" >
                    <input type="number" v-model.number="shippingRelease.delivery_special_3_zone_price"
                      style="display: none;" />
                    <input type="text" :value="shippingRelease.delivery_special_3_zone_price | currency" style="text-align:right;"
                      @input="$parent.addComma( shippingRelease, 'delivery_special_3_zone_price')" maxlength="10" />
                    원
                  </p>
                  <p v-else class="textWordWrap" style="background:#f1f1f1">
                  <input type="number" v-model.number="shippingRelease.delivery_special_3_zone_price"
                    style="text-align:right;"
                    disabled/>
                  원
                  </p>
              </td>
          </tr>
          </transition>
          <tr>
              <th>별도 설치비 유무<span class="necessary">*</span></th>
              <td colspan="3">
                  <div class="inputContainer">
                  <span class="inputWrap">
                    <input type="radio" :value=1 id="service02-01" v-model="shippingRelease.install_price_yn" >
                    <label for="service02-01">있음</label>
                  </span>
                  <span class="inputWrap">
                    <input type="radio" :value=0 id="service02-02" v-model="shippingRelease.install_price_yn">
                    <label for="service02-02">없음</label>
                  </span>
                </div>
              </td>
          </tr>
          <tr>
              <th>지역별 차등 배송비 정보 </th>
              <td colspan="3">
                  <div class="textareaWrap" style="width: 700px;">
                  <textarea rows="2" v-model="shippingRelease.delivery_region_price_text" maxlength="200"
                  ></textarea>
                  <p>{{delivery_region_price_textLen}} /200</p>
                </div>
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
import ModalBundleList from './ModalBundleList'
import ModalAddressList from './ModalAddressList'
import http from '@/http-commons'

import { eventBus } from '@/main'

let shippingInitData = {
  delivery_release_default_yn: 1, // 대표 출고지 설정
  delivery_name: '', // 배송지명
  delivery_zipcode_no: '', // 우편번호
  delivery_addr: '', // 주소
  delivery_detail_addr: '', // 주소 상세
  delivery_tel_no: '', // 전화번호1
  delivery_free_price: 0, // 무료 배송 기준
  delivery_default_price: 0, // 기본 배송비
  delivery_store_addr_no: '', // 스토어 주소 번호
  bundle_check: 1,
  delivery_bundle_group_code: null, // 묶음배송그룹코드
  delivery_bundle_cal_type_code: null, // 묶음배송 계산 방식
  delivery_special_2_zone_price_bundle: 0, // 묶음배송 도서 산간 추가 배송비 2권역
  delivery_special_3_zone_price_bundle: 0, // 묶음배송 도서 산간 추가 배송비 3권역
  delivery_special_2_zone_price: 0, // 묶음배송 미사용 도서 산간 추가 배송비 2권역
  delivery_special_3_zone_price: 0, // 묶음배송 미사용 도서 산간 추가 배송비 3권역
  delivery_region_price_text: '', // 지역별 배송비 정보
  install_price_yn: 0 // 설치비용 여부
}

export default {
  name: 'ShippingApiCreateRelease',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      delivery_special_2_zone_price_check: false, // 도서 산간 추가 배송비 2권역
      delivery_special_3_zone_price_check: false, // 도서 산간 추가 배송비 3권역
      delivery_special_2_zone_price_check_bundle: false, // 도서 산간 추가 배송비 2권역
      delivery_special_3_zone_price_check_bundle: false, // 도서 산간 추가 배송비 3권역
      shippingRelease: JSON.parse(JSON.stringify(shippingInitData)),
      valiAlert: {
        delivery_name: '배송지명을 입력해주세요.',
        delivery_zipcode_no: '우편번호를 입력해주세요.',
        delivery_addr: '주소를 입력해주세요.',
        delivery_detail_addr: '상세주소를 입력해주세요.',
        delivery_tel_no: '전화번호를 입력해주세요.',
        // delivery_free_price: '무료 배송 기준을 입력해주세요.',
        delivery_default_price: '기본배송비를 입력해주세요.'
      }
    }
  },
  created () {
  },
  beforeDestroy () {
    eventBus.$off('modalAddressListRelease')
    eventBus.$off('modalBundleList')
    eventBus.$off('commonServiceListChange')
    eventBus.$off('ReleaseUpdate')
    eventBus.$off('getRelease')
  },
  mounted () {
    // 수정시 탭 대표 출고지 데이터
    eventBus.$on('getRelease', (updateRelease) => {
      // 검색버튼 및 탭클릭으로 호출 됨
      console.log('탭 검샙 출고지')
      this.shippingRelease = updateRelease
      if (this.shippingRelease.delivery_special_2_zone_price_bundle > 0) {
        this.delivery_special_2_zone_price_check_bundle = true
      }
      if (this.shippingRelease.delivery_special_3_zone_price_bundle > 0) {
        this.delivery_special_3_zone_price_check_bundle = true
      }
      if (this.shippingRelease.delivery_special_2_zone_price > 0) {
        this.delivery_special_2_zone_price_check = true
      }
      if (this.shippingRelease.delivery_special_3_zone_price > 0) {
        this.delivery_special_3_zone_price_check = true
      }

      // this.get(storeDeliveryNo)
    })

    // 수정하기
    eventBus.$on('ReleaseUpdate', (name) => {
      this.save(name)
    })

    /** 스마트 스토어 주소지 선택 */
    eventBus.$on('modalAddressListRelease', (item) => {
      this.matchAddress(item)
    })
    /** 스마트 스토어 묶음배송비 선택 */
    eventBus.$on('modalBundleList', (item) => {
      this.shippingRelease.delivery_bundle_group_code = item.code
      if (item.value.bundleGroup && item.value.bundleGroup.calculationMethod) {
        this.shippingRelease.delivery_bundle_cal_type_code = parseInt(item.value.bundleGroup.calculationMethod)
      }
      // 2권역
      let area2ExtraFee = 0
      if (item.value.bundleGroup && item.value.bundleGroup.deliveryFeeByArea && item.value.bundleGroup.deliveryFeeByArea.area2ExtraFee) {
        area2ExtraFee = item.value.bundleGroup.deliveryFeeByArea.area2ExtraFee
        this.delivery_special_2_zone_price_check_bundle = true
        this.shippingRelease.delivery_special_2_zone_price_bundle = area2ExtraFee
      } else {
        this.delivery_special_2_zone_price_check = false
        this.shippingRelease.delivery_special_2_zone_price_bundle = area2ExtraFee
      }
      // 3권역
      let area3ExtraFee = 0
      if (item.value.bundleGroup && item.value.bundleGroup.deliveryFeeByArea && item.value.bundleGroup.deliveryFeeByArea.area3ExtraFee) {
        area3ExtraFee = item.value.bundleGroup.deliveryFeeByArea.area3ExtraFee
        this.delivery_special_3_zone_price_check_bundle = true
        this.shippingRelease.delivery_special_3_zone_price_bundle = area3ExtraFee
      } else {
        this.delivery_special_3_zone_price_check_bundle = false
        this.shippingRelease.delivery_special_3_zone_price_bundle = area3ExtraFee
      }

      this.$forceUpdate()
    })
    /** 공통 select box 변경시 init 상태로 data 변경 */
    eventBus.$on('commonServiceListChange', () => {
      this.shippingRelease = JSON.parse(JSON.stringify(shippingInitData))
      this.delivery_special_2_zone_price_check = false
      this.delivery_special_3_zone_price_check = false
      this.delivery_special_2_zone_price_check_bundle = false
      this.delivery_special_3_zone_price_check_bundle = false
    })
  },
  computed: {
    delivery_default_price_disable () {
      let disableYn = true
      if (this.shippingRelease.delivery_free_price >= 2500) {
        disableYn = false
      } else {
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        this.shippingRelease.delivery_default_price = 0
      }
      return disableYn
    },
    delivery_region_price_textLen () {
      let textLen = 0
      if (this.shippingRelease.delivery_region_price_text !== '') {
        textLen = this.shippingRelease.delivery_region_price_text.length
      }
      return textLen
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
    matchAddress (item) {
      console.log('matchAddress')
      this.shippingRelease.delivery_name = item.name
      this.shippingRelease.delivery_detail_addr = item.detailAddress
      this.shippingRelease.delivery_addr = item.baseAddress
      this.shippingRelease.delivery_tel_no = item.phoneNumber1
      this.shippingRelease.delivery_zipcode_no = item.postalCode
      this.shippingRelease.delivery_store_addr_no = item.addressId
      this.$forceUpdate()
    },
    save (name) {
      if (!this.validationCheck()) return false

      let mallNo = this.$store.state.searchParam.mall_no
      let form = new FormData()
      form.append('mall_no', mallNo)
      form.append('delivery_name', this.shippingRelease.delivery_name)
      form.append('delivery_tel_no', this.shippingRelease.delivery_tel_no)
      form.append('delivery_zipcode_no', this.shippingRelease.delivery_zipcode_no)
      form.append('delivery_addr', this.shippingRelease.delivery_addr)
      form.append('delivery_detail_addr', this.shippingRelease.delivery_detail_addr)
      form.append('delivery_free_price', this.shippingRelease.delivery_free_price)
      form.append('delivery_default_price', this.shippingRelease.delivery_default_price)

      if (this.shippingRelease.bundle_check === 1) { // 묶음배송 사용
        form.append('delivery_bundle_group_code', this.shippingRelease.delivery_bundle_group_code)
        if (this.shippingRelease.delivery_special_2_zone_price_bundle !== 0 && this.delivery_special_2_zone_price_check_bundle === true) {
          form.append('delivery_special_2_zone_price', this.shippingRelease.delivery_special_2_zone_price_bundle)
        }
        if (this.shippingRelease.delivery_special_2_zone_price_bundle !== 0 && this.shippingRelease.delivery_special_3_zone_price_bundle !== 0 &&
          this.delivery_special_2_zone_price_check_bundle === true && this.delivery_special_3_zone_price_check_bundle === true) {
          form.append('delivery_special_3_zone_price', this.shippingRelease.delivery_special_3_zone_price_bundle)
        }
        if (this.shippingRelease.delivery_bundle_cal_type_code) {
          form.append('delivery_bundle_cal_type_code', this.shippingRelease.delivery_bundle_cal_type_code)
        }

        form.append('delivery_region_price_text', this.shippingRelease.delivery_region_price_text)
      } else { // 묶음배송 미사용
        if (this.shippingRelease.delivery_special_2_zone_price !== 0 && this.delivery_special_2_zone_price_check === true) {
          form.append('delivery_special_2_zone_price', this.shippingRelease.delivery_special_2_zone_price)
        }
        if (this.shippingRelease.delivery_special_2_zone_price !== 0 && this.shippingRelease.delivery_special_3_zone_price !== 0 &&
          this.delivery_special_2_zone_price_check === true && this.delivery_special_3_zone_price_check === true) {
          form.append('delivery_special_3_zone_price', this.shippingRelease.delivery_special_3_zone_price)
        }

        form.append('delivery_region_price_text', this.shippingRelease.delivery_region_price_text)
      }

      form.append('install_price_yn', this.shippingRelease.install_price_yn)
      form.append('delivery_release_default_yn', this.shippingRelease.delivery_release_default_yn)
      form.append('delivery_store_addr_no', this.shippingRelease.delivery_store_addr_no)

      form.append('user_code', 1)
      form.append('user_no', this.userNo)

      this.isLoading = true

      form.append('store_delivery_no', this.shippingRelease.store_delivery_no)
      let url = '/smartstore/shipping/api/updaterelease'

      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          if (response.data.data === 1) {
            this.$modal.show(ModalAlert,
              {title: '', text: '대표출고지 정보를 수정하였습니다.'},
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
        if (item === 'delivery_default_price') { // 기본배송비
          if (this.shippingRelease['delivery_free_price'] !== 0) { // 무료배송기준
            if (!this.shippingRelease[item]) {
              this.$modal.show(ModalAlert,
                { title: '대표 출고지 설정', text: this.valiAlert[item] },
                { width: 450, height: 'auto' }
              )
              return false
            }
          } else {
            // 기본배송비는 무료배송기준이 0이 아닐 경우에만 validation 체크
            continue
          }
        }

        if (!this.shippingRelease[item]) {
          this.$modal.show(ModalAlert,
            { title: '대표 출고지 설정', text: this.valiAlert[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }

        if (this.shippingRelease.bundle_check === 1) {
          if (!this.shippingRelease.delivery_bundle_group_code) {
            this.$modal.show(ModalAlert,
              { title: '대표 출고지 설정', text: '묶음배송 조회 후 그룹을 선택해주세요.' },
              { width: 450, height: 'auto' }
            )
            return false
          }
        }
      }
      return isCheck
    },
    getBundleList () {
      const mallId = this.$store.state.searchParam.mall_id
      if (mallId === '0') {
        this.$modal.show(ModalAlert,
          { title: '', text: '자사몰명을 선택해주세요.' }, { width: 450, height: 'auto' }
        )
        return
      }

      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/shipping/bundlelist/' + mallId
      this.isLoading = true
      http
        .get(url)
        .then(response => {
          this.isLoading = false
          let list = response.data.bundleGroupList.bundleGroup

          this.$modal.show(ModalBundleList,
            { list: list, userMenuAuth: this.userMenuAuth },
            { width: '50%', height: 'auto', clickToClose: false, scrollable: true }
          )
          console.log(response)
        })
        .catch(e => {
          // console.log(e)
          this.isLoading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
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
            { list: list, userMenuAuth: this.userMenuAuth, whereOpen: 'ShippingApiCreateRelease' },
            { width: '50%', height: 'auto', clickToClose: false, scrollable: true }
          )
          console.log(response)
        })
        .catch(e => {
          // console.log(e)
          this.isLoading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    checkAddPrice (event, name) {
      // 묶음배송미사용
      if (name === 'delivery_special_2_zone_price') { // 2권역 체크시 3권역 선택 가능
        this.delivery_special_2_zone_price_check = event.target.checked
        if (event.target.checked === false) { // 2권역 끌때 2권역 체크 및 3권역 체크 다 해제
          this.delivery_special_3_zone_price_check = event.target.checked
        }
        this.$forceUpdate()
      }
      if (name === 'delivery_special_3_zone_price') { // 2권역 체크시 3권역 선택 가능
        this.delivery_special_3_zone_price_check = event.target.checked
        this.$forceUpdate()
      }
      // 묶음배송비
      if (name === 'delivery_special_2_zone_price_bundle') { // 2권역 체크시 3권역 선택 가능
        this.delivery_special_2_zone_price_check = event.target.checked
        if (event.target.checked === false) { // 2권역 끌때 2권역 체크 및 3권역 체크 다 해제
          this.delivery_special_3_zone_price_check = event.target.checked
        }
        this.$forceUpdate()
      }
      if (name === 'delivery_special_3_zone_price_bundle') { // 2권역 체크시 3권역 선택 가능
        this.delivery_special_3_zone_price_check = event.target.checked
        this.$forceUpdate()
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
      // this.showTabName = tabName
    }
  },
  props: {
    'userMenuAuth': Object
  }
}
</script>

<style scoped>

.fade-enter-active,
.fade-leave-active {
    transition: opacity .3s
}

.fade-enter,
.fade-leave-to {
    opacity: 0
}

.slide-fade-enter-active {
  transition: all .3s ease;
}
.slide-fade-leave-active {
  transition: all .3 cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to
/* .slide-fade-leave-active below version 2.1.8 */ {
  transform: translateX(10px);
  opacity: 0;
}
</style>
