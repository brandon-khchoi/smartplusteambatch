<template>
  <div class="SellerCreate">
    <div class="content">

      <template v-if="$route.params.mall_no">
        <h1>판매자 연동 수정</h1>
      </template>
      <template v-else>
        <h1>신규 판매자 연동 등록</h1>
      </template>
      <div class="formContainer">
        <article>
          <div class="articleWrap">
            <CommonServiceList :propMallNo="$route.params.mall_no" v-if="$route.params.mall_no"></CommonServiceList>
            <div class="tableWrap tableRegister ">

              <table summary="신규 판매자 연동 등록">
                  <caption class="blind">신규 판매자 연동 등록</caption>
                  <colgroup>
                      <col style="width:200px"/>
                      <col style="width:340px"/>
                      <col style="width:150px"/>
                      <col/>
                  </colgroup>
                  <tbody>
                  <tr v-if="!$route.params.mall_no">
                    <th>자사몰명</th>
                    <td colspan="3">
                      <div class="searchBtnWrap single">
                        <div @click ="showCorpSearch" style="display: inline-block; cursor: pointer">
                          <input
                            type="text"
                            placeholder="법인/상호명을 검색해주세요."
                            v-model="searchTxt"
                            disabled
                            style="pointer-events: none;width:300px;background:#f1f1f1;"
                          >
                        </div>
                        <p class="btn_search" @click="showCorpSearch">찾기</p>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th style="border-top: 0px;">연동 쇼핑몰명<span class="necessary">*</span></th>
                    <td style="border-top: 0px;" colspan="3">
                      <select v-model="seller.store_no" style="width:300px" >
                        <option :value="seller.store_no" >
                          스마트스토어
                        </option>
                      </select>
                    </td>
                  </tr>
                  <tr>
                    <th>호스팅 업체<span class="necessary">*</span></th>
                    <td colspan="3">
                      <div class="inputContainer">
                        <span class="inputWrap">
                          <input type="radio" name="service01" id="service01-01"
                            :value=1
                            v-model="seller.cal_code">
                          <label for="service01-01">카페24</label>
                        </span>
                        <span class="inputWrap">
                          <input type="radio" name="service01" id="service01-02"
                            :value=2
                            v-model="seller.cal_code">
                          <label for="service01-02">메이크샵</label>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th>A/S 전화번호<span class="necessary">*</span></th>
                    <td colspan="3">
                      <input type="text" @input="typing($event, 'notChar', 'seller', 'mall_as_tel_no')"
                        v-model="seller.mall_as_tel_no"
                        maxlength="50"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th>A/S 내용<span class="necessary"></span></th>
                    <td colspan="3">
                      <div class="textareaWrap" style="width: 700px;">
                        <textarea rows="5" v-model="seller.mall_as_text" maxlength="500"
                        ></textarea>
                        <p>{{mall_as_textLen}} /500</p>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th>API 판매자 ID<span class="necessary">*</span></th>
                    <td colspan="3">
                      <input type="text"
                        v-model="seller.api_sell_id"
                        maxlength="50"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th>API 키<span class="necessary">*</span></th>
                    <td colspan="3">
                      <input type="text"
                        v-model="seller.api_key"
                        maxlength="50"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th>자사몰 상품상세 URL<span class="necessary">*</span></th>
                    <td colspan="3">
                      <input type="text"
                        v-model="seller.product_mall_url_addr"
                        maxlength="200"
                        placeholder="http://www.lunasoft.com/shop/shopdetail.html?branduid="
                        style="width: 500px;"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th>스마트스토어 상품상세 URL<span class="necessary">*</span></th>
                    <td colspan="3">
                      <input type="text"
                        v-model="seller.product_store_url_addr"
                        maxlength="200"
                        placeholder="https://smartstore.naver.com/lunasoft/products/"
                        style="width: 500px;"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th>자사몰 상품 등록 기준일</th>
                    <td colspan="3">
                      <span class="inputWrap">
                        <input type="checkbox"
                          v-model="isFirstSmartstoreYn"
                          id = "service03-01">
                        <label for="service03-01">연동 기준일자</label>
                      </span>
                      <div class="calendarWrapper">
                      <template v-if="isFirstSmartstoreYn">

                        <SingleDatepickerAll
                          :propDate="startDate.date"
                          v-on:update:startDate="setStartDate"
                        />
                       </template>
                      <template v-else>
                        <input
                          type="text"
                          placeholder="예) 2019-07-27"
                          v-model="startDate.date"
                          style="background:#f1f1f1;"
                          readonly
                        >
                        <img class="ui-datepicker-trigger" src="//static.lunasoft.co.kr/lunaadmin/images/calendar.png" >
                      </template>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th>자동 연동 사용여부</th>
                    <td colspan="3">
                      <p class="toggleWrap">
                        <label class="switch">
                          <input type="checkbox" :true-value=1 :false-value=0 v-model="seller.use_yn">
                          <span class="slider"></span>
                        </label>
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <th>초기 연동시 상품가격 설정<span class="necessary">*</span></th>
                    <td colspan="3">
                      <div class="inputContainer">
                        <span class="inputWrap">
                          <input type="radio" name="service02" id="service02-01"
                            :value=1
                            v-model="seller.product_init_price_type_code">
                          <label for="service02-01">판매가 적용</label>
                        </span>
                        <span class="inputWrap">
                          <input type="radio" name="service02" id="service02-02"
                            :value=2
                            v-model="seller.product_init_price_type_code">
                          <label for="service02-02">할인가 적용 </label>
                        </span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th>연동시 이미지 설정<span class="necessary">*</span>
                      <i class="far fa-question-circle" @click="$bvToast.show('example-toast')" style="padding:8px;"></i>
                      <div class="bootstrapWrapper "  >
                        <b-toast id="example-toast"
                          static
                          no-auto-hide style="position:absolute;">
                          <div >
                          <div style="font-size:1rem">
                            메이크샵
                            <br/>
                            detail이미지 -> 상세이미지(mini이미지)
                            <br/>
                            list이미지 -> 확대이미지(max이미지)
                            <br/>
                            tiny이미지 -> 리스트/메인 이미지(tiny이미지)
                            <br/>
                            <br>
                          </div>
                          <div style="font-size:1rem">
                            카페24
                            <br/>
                            detail이미지 -> 상세이미지(mini이미지)
                            <br/>
                            list이미지 -> 확대이미지(max이미지)
                            <br/>
                            tiny이미지 -> 리스트/메인 이미지(tiny이미지)
                            <br/>
                          </div>
                          </div>
                        </b-toast>
                      </div>
                    </th>
                    <td colspan="3">
                      <div class="inputContainer">
                        <span class="inputWrap">
                          <input type="radio" name="service04" id="service04-01"
                            :value=1
                            v-model="seller.product_image_type_code">
                          <label for="service04-01">detail이미지</label>
                        </span>
                        <span class="inputWrap">
                          <input type="radio" name="service04" id="service04-02"
                            :value=2
                            v-model="seller.product_image_type_code">
                          <label for="service04-02">list이미지</label>
                        </span>
                        <span class="inputWrap">
                          <input type="radio" name="service04" id="service04-03"
                            :value=3
                            v-model="seller.product_image_type_code">
                          <label for="service04-03">tiny이미지</label>
                        </span>

                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th>사이즈 이미지 정보<span class="necessary">*</span></th>
                    <td colspan="3">
                      <!-- <select v-model="seller.image_type" style="width:300px" @change="changeMallInfo($event)" >
                        <option value="" >선택하세요</option>
                        <option value="IMAGE" >IMAGE</option>
                        <option value="SNAPFIT" >SNAPFIT</option>
                        <option value="CREMA" >CREMA</option>
                        <option value="HTML" >HTML</option>
                      </select> -->
                      <span class="inputWrap">
                          <input type="checkbox" id="service05-01" value="IMAGE" v-model="seller.image_typeArr" >
                          <label for="service05-01">IMAGE</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="service05-02" value="SNAPFIT" v-model="seller.image_typeArr" >
                          <label for="service05-02">SNAPFIT</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="service05-03" value="CREMA" v-model="seller.image_typeArr" >
                          <label for="service05-03">CREMA</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="service05-04" value="HTML" v-model="seller.image_typeArr" >
                          <label for="service05-04">HTML</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="service05-05" value="CRAWLING" v-model="seller.image_typeArr" >
                          <label for="service05-05">CRAWLING</label>
                        </span>
                    </td>
                  </tr>
                  <tr>
                    <th>가격비교</th>
                    <td colspan="3">
                      <span class="inputWrap">
                        <input type="checkbox" id="service06-01" :true-value=1 :false-value=0 v-model="seller.navershopping_link_yn" >
                        <label for="service06-01">연동 여부</label>
                      </span>
                      <span style="color:green;">
                        ※ 네이버쇼핑 서비스반영은 약 2시간 ~ 8시간 정도 소요됩니다.
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <th>상품상세 url 변경</th>
                    <td colspan="3">
                      <span class="inputWrap">
                        <input type="checkbox" id="service07-01" :true-value=1 :false-value=0 v-model="seller.product_detail_change_use_yn" >
                        <label for="service07-01">상세 url 변경</label>
                      </span>
                      <br>
                      <input type="text"
                        v-model="seller.product_detail_replace_link"
                        maxlength="200"
                        placeholder="변경할 url 링크"
                        style="width: 500px;"
                      />
                      <input type="text"
                        v-model="seller.product_detail_change_link"
                        maxlength="200"
                        placeholder="변경될 url 링크"
                        style="width: 500px;"
                      />
                    </td>
                  </tr>
                  <tr v-if="$route.params.mall_no">
                    <th>수기등록 상품 가져오기</th>
                    <td colspan="3">
                      <button type="button" class="btn_min" v-if="userMenuAuth.create_data"
                        @click="clickGetProuct() " >상품 가져오기
                      </button>
                    </td>
                  </tr>
                  </tbody>
              </table>

          </div>
          </div>
        </article>
        <div class="btnWrap btnEnd" style="text-align:center;">
          <ul>
            <li>
              <input class="btn_cancel btn-lg" type="button" value="목록" @click="backBtn"/>
            </li>
            <li>
              <template v-if="$route.params.mall_no">
                <input v-if="userMenuAuth.modify_data" class="btn_save btn-lg" type="button" value="수정" @click="save('update')"/>
              </template>
              <template v-else>
                <input v-if="userMenuAuth.create_data" class="btn_save btn-lg" type="button" value="등록" @click="save('create')"/>
              </template>
            </li>
          </ul>
          <br>
          <br>
          <br>
          <br>
        </div>
      </div>
    </div>
    <v-dialog :clickToClose=false />
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import SingleDatepickerAll from '@/components/common/SingleDatepickerAll'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalCorpSearch from '@/components/common/modal/ModalCorpSearch'
import CommonServiceList from './CommonServiceList'
import http from '@/http-commons'
import { eventBus } from '@/main'

export default {
  name: 'SellerCreate',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      searchTxt: '',
      mallSearchInfo: {
        biz_name: ''
      },
      isFirstSmartstoreYn: false,
      startDate: {
        date: '',
        hour: '',
        time: ''
      },
      seller: {
        store_no: 1, // 스토어 번호(네이버 스마트스토어)
        cal_code: 1, // 상품연동계산 방식
        mall_as_tel_no: '',
        mall_as_text: '',
        api_sell_id: '', // API 판매자 ID
        api_key: '', // API id
        api_product_use_yn: 1, // 상품 API 사용 여부
        api_order_use_yn: 1, // 주문 API 사용 여부
        product_mall_url_addr: '',
        product_store_url_addr: '',
        product_init_price_type_code: 1, // 상품 초기 연동시 가격 타입
        product_image_type_code: 1, // 상품 이미지 타입(tb_product_info), 1:file_image_detail_text, 2:file_image_list_text, 3:file_image_tiny_text
        image_type: '',
        image_typeArr: [],
        product_detail_replace_link: '',
        product_detail_change_link: '',
        product_detail_change_use_yn: 0,
        navershopping_link_yn: 0,
        use_yn: 0
      },
      valiAlert: {
        store_no: '연동사를 선택해주세요.',
        cal_code: '상품연동계산 방식을 선택해주세요.',
        mall_as_tel_no: 'AS전화번호를 입력해주세요.',
        api_sell_id: 'API 판매자 ID 를 입력해주세요.', // API 판매자 ID
        api_key: 'API 키를 입력해주세요.', // API id
        image_type: '사이즈 이미지 정보를 선택해주세요.', // 사이즈 이미지
        product_mall_url_addr: '자사몰 상품상세 URL 을 입력해주세요.',
        product_store_url_addr: '스마트스토어 상품상세 URL 을 입력해주세요.',
        product_detail_replace_link: '[상세 url 변경]을 체크하신 경우 [변경할 url 링크]를 입력해주세요.',
        product_detail_change_link: '[상세 url 변경]을 체크하신 경우 [변경될 url 링크]를 입력해주세요.'
      }
    }
  },
  computed: {
    mall_as_textLen () {
      let textLen = 0; if (this.seller.mall_as_text !== '') textLen = this.seller.mall_as_text.length
      return textLen
    }
  },
  mounted () {
    if (this.$route.params.mall_id && this.$route.params.mall_no && this.$route.params.store_no) { // update 로 들어온 경우
      this.get(this.$route.params.mall_id, this.$route.params.mall_no, this.$route.params.store_no)
    }
    eventBus.$on('setCorpSearchInfo', item => this.setCorpSearchInfo(item)) // 검색내용
  },
  methods: {
    setStartDate (value) {
      this.startDate.date = value
    },
    typing: function (event, type, item, name) {
      let patternNo = /^[0-9]+$/
      var patternChar = /[a-zA-Z]/
      // let charPattern = /[`~!@#$%^&*|\\\'\";:\/?]/gi
      let charPattern1 = /[`~!@#$%^&*-|;:?]/gi

      let message = event.target.value.trim()
      console.log(message)
      if (type === 'notChar') {
        // 숫자가 아니면 지움, 특수문자가 아니면 지움, 문자가 하나라도 들어가면 지움
        if ((!patternNo.test(message) && !charPattern1.test(message)) || patternChar.test(message)) {
          this[item][name] = message.substr(0, message.length - 1)
        }
      }
    },
    showCorpSearch () {
      // console.log('showCorpSearch')
      this.$modal.show(
        ModalCorpSearch,
        {
          propSearchTxt: this.mallSearchInfo.biz_name,
          propIsDeliveryService: false
        },
        {
          width: 1200,
          height: 'auto',
          clickToClose: false,
          // draggable: true,
          scrollable: true
        }
      )
    },
    setCorpSearchInfo (item) { // 초기 등록시 몰 정보 검색후 return 데이터
      console.log('setCorpSearchInfo')
      console.log(item)
      this.mallSearchInfo = item
      this.searchTxt = this.mallSearchInfo.biz_name + '/' + this.mallSearchInfo.mall_name

      this.$store.state.searchParam = {}
      this.$store.state.searchParam.mall_id = this.mallSearchInfo.mall_id
      this.$store.state.searchParam.mall_no = this.mallSearchInfo.mall_no
    },
    backBtn () {
      if (this.$route.params.mall_no && this.$route.params.store_no) { // update 로 들어온 경우
        this.$router.push({name: 'sellerlist', query: { mall_no: this.$store.state.searchParam.mall_no }})
      } else {
        this.$router.push({name: 'sellerlist'})
      }
    },
    get (mallId, mallNo, storeNo) {
      let url = '/smartstore/sellerregister/info/' + mallId + '/' + mallNo + '/' + storeNo
      console.log(url)
      http
        .get(url)
        .then(response => {
          this.seller = response.data.data
          if (this.seller.product_standard_register_date) {
            this.isFirstSmartstoreYn = true
            // eslint-disable-next-line no-undef
            this.startDate.date = dateFormatToString(this.seller.product_standard_register_date)
          }
          this.seller.image_typeArr = this.seller.image_type.split('|')
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
          this.backBtn()
        })
    },
    save (name) {
      console.log(this.$store.state.searchParam)
      if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
        this.$modal.show(ModalAlert,
          {title: '', text: '쇼핑몰을 선택해주세요.'},
          { width: 450, height: 'auto' }
        )
        return
      }
      this.seller.image_type = this.seller.image_typeArr.join('|')
      if (!this.validationCheck()) return false

      let mallNo = this.$store.state.searchParam.mall_no
      let mallId = this.$store.state.searchParam.mall_id
      let form = new FormData()
      form.append('mall_no', mallNo)
      form.append('mall_id', mallId)
      form.append('hosting_no', this.$store.state.searchParam.hosting_no)
      form.append('store_no', this.seller.store_no)
      form.append('cal_code', this.seller.cal_code)
      form.append('mall_as_tel_no', this.seller.mall_as_tel_no)
      if (this.seller.mall_as_text === '') {
      } else {
        form.append('mall_as_text', this.seller.mall_as_text)
      }
      form.append('api_key', this.seller.api_key)
      form.append('api_sell_id', this.seller.api_sell_id)
      form.append('api_order_use_yn', this.seller.api_order_use_yn)
      form.append('api_product_use_yn', this.seller.api_product_use_yn)
      form.append('product_mall_url_addr', this.seller.product_mall_url_addr)
      form.append('product_store_url_addr', this.seller.product_store_url_addr)
      if (this.isFirstSmartstoreYn) {
        this.seller.product_standard_register_date = this.startDate.date
        form.append('product_standard_register_date', this.seller.product_standard_register_date)
      }
      form.append('product_init_price_type_code', this.seller.product_init_price_type_code)
      form.append('product_image_type_code', this.seller.product_image_type_code)
      form.append('navershopping_link_yn', this.seller.navershopping_link_yn)
      if (this.seller.product_detail_replace_link) {
        form.append('product_detail_replace_link', this.seller.product_detail_replace_link)
      }
      if (this.seller.product_detail_change_link) {
        form.append('product_detail_change_link', this.seller.product_detail_change_link)
      }
      form.append('product_detail_change_use_yn', this.seller.product_detail_change_use_yn)
      form.append('image_type', this.seller.image_type)
      form.append('use_yn', this.seller.use_yn)
      form.append('user_code', 1)
      form.append('user_no', this.userNo)

      this.isLoading = true

      let url = ''
      if (name === 'update') {
        url = '/smartstore/sellerregister/update'
      } else if (name === 'create') {
        url = '/smartstore/sellerregister/create'
      }

      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          if (response.data.data === 1) {
            if (name === 'update') {
              this.$modal.show(ModalAlert,
                {title: '', text: '판매자 연동 정보를 수정하였습니다.'},
                { width: 450, height: 'auto' }
              )
            } else if (name === 'create') {
              this.$modal.show(ModalAlert,
                {title: '', text: '신규 판매자 연동 정보를 등록되었습니다.'},
                { width: 450, height: 'auto' }
              )
            }

            this.$router.push({ name: 'sellerlist', query: { mall_no: this.$store.state.searchParam.mall_no } })
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
          if (e.response.data.responseCode === -2) {
            this.$modal.show(ModalAlert,
              { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
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
        if (!this.seller[item]) {
          if (item === 'product_detail_replace_link' || item === 'product_detail_change_link') {
            if (this.seller.product_detail_change_use_yn === 1) {
              this.$modal.show(ModalAlert,
                { title: '', text: this.valiAlert[item] },
                { width: 450, height: 'auto' }
              )
              return false
            } else {
              continue
            }
          }
          this.$modal.show(ModalAlert,
            { title: '', text: this.valiAlert[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return isCheck
    },
    clickGetProuct () {
      this.$modal.show('dialog', {
        title: '수기등록 상품',
        text: '수기등록된 상품을 가져오시겠습니까?',
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
            this.getSmartStoreProductList()
            this.$modal.hide('dialog')
          }
        }]
      })
    },
    getSmartStoreProductList () {
      const mallId = this.$store.state.searchParam.mall_id
      // eslint-disable-next-line no-undef
      let getSmartStoreProductUrl = smartStroreApiUrl + '/smartstore/insertproductlist/' + mallId
      console.log('mallId : ' + mallId + ' || getSmartStoreProductUrl : ' + getSmartStoreProductUrl)

      // eslint-disable-next-line no-undef
      let apiUpdateDate = dateTimeFormatToString(new Date())
      let params = {
        user_code: 1,
        user_no: this.userNo,
        api_update_date: apiUpdateDate
      }
      http
        .post(getSmartStoreProductUrl, params)
        .then()

      this.$modal.show(ModalAlert,
        { title: '', text: '수기등록 상품을 가져오고 있습니다.' }, { width: 450, height: 'auto' }
      )

      /** ImageAgent 호출 */
      // let imageAgentUrl = 'http://teiagent.lunasoft.co.kr/allProductUpdate?mallId=' + mallId
      // http
      //   .post(imageAgentUrl)
      //   .then()
    }
  },
  components: {
    CommonServiceList,
    SingleDatepickerAll
  },
  props: {
    'userMenuAuth': Object
  }
}
</script>
<style scoped lang="scss">
  .bootstrapWrapper .toast {
    max-width: 600px !important;
        font-size: 1rem;
  }
</style>
