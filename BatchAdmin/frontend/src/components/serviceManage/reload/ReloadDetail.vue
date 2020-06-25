<template>
  <div class="content service setting option ">
    <h1>리로드 서비스 세팅</h1>
    <div class="formContainer">
      <article>
        <div class="articleWrap">
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">서비스 오픈 설정</p>
            </div>
            <div class="detailContent">
              <div class="tableWrap ">
                <table summary="서비스 오픈 설정">
                  <caption class="blind">서비스 오픈 설정</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:calc(50% - 100px)">
                    <col style="width:200px">
                    <col>
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>서비스 사용 상태<span class="necessary">*</span></th>
                      <td>
                        <div class="searchBtnWrap single">
                          <select v-model="resultData.service_stat_code">
                            <option value="" disabled>선택하세요.</option>
                            <option value=1>오픈예정</option>
                            <option value=2>사용중</option>
                            <option value=3>일시정지</option>
                            <option value=4>정지</option>
                          </select>
                        </div>
                      </td>
                      <th>리로드 KEY<span class="necessary">*</span></th>
                      <td>
                        <div class="searchBtnWrap single">
                          <input type="text" style="width:80%;"
                                 v-model="resultData.reload_key" disabled>
                          <p class="btn_search" @click="funcKeySetting">생성 요청</p>
                        </div>
                      </td>
                    </tr>

                    <tr v-show="Number(resultOrgData.service_stat_code) === 1">
                      <th>서비스 사용 기간<span class="necessary">*</span></th>
                      <td colspan="3">
                        <datepicker
                        class="datepicker"
                        :language="languages['ko']"
                        format="yyyy-MM-dd"
                        v-model="firstSettingStartDate"
                        />
                        <span>~</span>
                        <datepicker
                        class="datepicker"
                        :language="languages['ko']"
                        format="yyyy-MM-dd"
                        disabled
                        v-model="resultData.service_end_date"
                        />
                        <span style="margin-left: 20px;">14일(무료사용기간)</span>
                      </td>
                    </tr>
                    <tr>
                      <th>서비스 사용 시작일<span class="necessary">*</span></th>
                      <td colspan="3">
                        <datepicker
                        class="datepicker"
                        :language="languages['ko']"
                        format="yyyy-MM-dd"
                        disabled
                        v-model="resultData.service_first_date"
                        />

                      </td>
                    </tr>
                    <tr>
                      <th>통계 간접 기간 <!-- i id="switchDayTooltip" class="far fa-question-circle"></i -->
                      <span class="necessary">*</span></th>
                      <td colspan="3">
                        <input type="number" v-model="resultData.product_buy_switch_day" style="width:100px;"> 일
                      </td>
                    </tr>
                    <tr>
                      <th><p>쇼핑몰 정보<span class="necessary">*</span></p>({{propSearchMallData.mall_name}} / {{propSearchMallData.member_id}})</th>
                      <td colspan="3">
                        <div>
                          쇼핑몰 이름 : <input type="text" :value="propSearchMallData.mall_name" style="width:200px">
                          쇼핑몰 대표 URL :<input type="text" v-model="resultData.reload_url_addr">
                          쇼핑몰 모바일 URL :<input type="text" v-model="resultData.reload_mobile_url_addr">
                        </div>
                        <div style="margin-top:10px;">
                          <h4>올바른 값: imvely.com(O) 잘못된 값: www.imvely.com(X)</h4>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <!-- <b-tooltip target="switchDayTooltip" triggers="hover">
            <div style="text-align: left; font-size: 15px; background-color:white; padding:10px; border: 1px solid black; border-radius: 10px;">
              리로드 클릭 후 구매금액 및 구매전환 통계로 계산되는 간접 기간.
              <br/>
              (직접 기간의 경우 클릭 후 60분 이내)
            </div>
          </b-tooltip> -->
<!---->
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">상품 연동 정보</p>
            </div>
            <div class="detailContent">
              <div class="tableWrap">
                <table summary="상품 연동 정보">
                  <caption class="blind">상품 연동 정보</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:100%">
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>이미지 타입<span class="necessary">*</span></th>
                      <td>
                        <span class="inputWrap">
                          <input type="checkbox" id="dtImgType1" v-model="resultData.product_image_type_code" :true-value="1" :false-value="0" >
                          <label for="dtImgType1">상세이미지</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="dtImgType2" v-model="resultData.product_image_type_code" :true-value="3" :false-value="0" >
                          <label for="dtImgType2">목록이미지</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="dtImgType3" v-model="resultData.product_image_type_code" :true-value="2" :false-value="0" >
                          <label for="dtImgType3">작은목록이미지</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="dtImgType4" v-model="resultData.product_image_type_code" :true-value="4" :false-value="0" >
                          <label for="dtImgType4">축소이미지</label>
                        </span>
                      </td>
                    </tr>
                    <tr>
                      <th>가격 노출 타입<span class="necessary">*</span></th>
                      <td>
                        <span class="inputWrap">
                          <input type="checkbox" id="priceType1" v-model="resultData.product_price_type_code" :true-value="1" :false-value="0" >
                          <label for="priceType1">판매가격</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="priceType2" v-model="resultData.product_price_type_code" :true-value="2" :false-value="0" >
                          <label for="priceType2">할인가격</label>
                        </span>
                      </td>
                    </tr>
                    <tr>
                      <th>상품 상세 URL 포맷<span class="necessary">*</span></th>
                      <td>
                        <input type="text" style="width:100%;" v-model="resultData.product_url_addr"
                         placeholder="http://lunasoft.co.kr/product/detail.html?product_no={product_no}">
                      </td>
                    </tr>
                    <tr>
                      <th>상품카테고리</th>
                      <td>
                        <select v-show="productCategoryInfoData"
                                v-model="selectCategoryNo"
                                style="width:15%;">
                          <option value="">선택하세요</option>
                          <template v-for="item in productCategoryInfoData">
                          <option :key="item.category_no" :value="item.category_no">{{item.category_name}}</option>
                          </template>
                        </select>
                        <input type="text" style="float:right;" hidden>
                        <p v-show="productCategoryInfoData"
                           class="btn_search"
                           style="margin-left: 30px;"
                           @click="funcProductRelInfo">미리보기</p>
                           <p v-show="!productCategoryInfoData">{{categoryInfoDataMsg}}</p>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="4" class="textRight">
                        <input class="btn_delete" type="button" @click="funcReloadSetting(1)" value="저장" >
                        <input v-show="resultOrgData.service_stat_code !== 2" class="btn_delete" type="button" @click="funcReloadServiceStart" value="서비스 시작">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
<!---->
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">사용 기간 설정</p>
            </div>
            <div class="detailContent">
              <div class="tableWrap ">
                <table summary="사용 기간 설정">
                  <caption class="blind">사용 기간 설정</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:calc(50% - 100px)">
                    <col style="width:200px">
                    <col>
                  </colgroup>
                  <tbody>
                    <tr>
                      <th rowspan="2">사용기간 연장<span class="necessary">*</span></th>
                      <td colspan="3">
                        <datepicker
                        class="datepicker"
                        :language="languages['ko']"
                        format="yyyy-MM-dd"
                        v-model="resultData.service_start_date"
                        />
                        <span>~</span>
                        <datepicker
                        class="datepicker"
                        :language="languages['ko']"
                        format="yyyy-MM-dd"
                        disabled
                        v-model="resultData.service_end_date"
                        />
                        <input type="button" class="btn_calendar" value="+14일" @click="funcServiceDateAdd(14)" style="margin-left: 30px;">
                        <input type="button" class="btn_calendar" value="+30일" @click="funcServiceDateAdd(30)">
                        <input type="button" class="btn_calendar" value="+180일" @click="funcServiceDateAdd(180)">
                        <input type="button" class="btn_calendar" value="+365일" @click="funcServiceDateAdd(365)">
                        <input type="number" v-model="serviceDay" style="width:100px; margin-left: 30px;">일
                      </td>
                    </tr>
                    <tr>
                      <td colspan="3">
                        <select v-model="historyMemoCode" style="width: 10%;">
                          <option value=1>서비스연장</option>
                          <option value=2>서비스차감</option>
                          <option value=3>서비스중지</option>
                          <option value=4>기타</option>
                        </select>
                        <input type="text" style="width: 50%" v-model="historyMemo">
                        <input type="text" style="float:right;" hidden>
                        <p class="btn_search" style="margin-left: 30px;" @click="funcServiceHistory">이력 보기</p>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="4" class="textRight">
                        <input class="btn_delete" type="button" @click="funcReloadCancel(2)" value="취소">
                        <input class="btn_delete" type="button" @click="funcReloadSetting(2)" value="적용">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
<!---->
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">기타 설정</p>
            </div>
            <div class="detailContent">
              <div class="tableWrap">
                <table summary="기타 설정">
                  <caption class="blind">기타 설정</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:calc(50% - 200px)">
                    <col style="width:200px">
                    <col style="width:calc(50% - 200px)">
                    <col style="width:200px">
                    <col style="width:calc(50% - 200px)">
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>담당자 정보<span class="necessary">*</span></th>
                      <td colspan="5">
                        메일 :<input type="text" v-model="resultData.service_expire_guide_email_addr">
                        HP :<input type="text" v-model="resultData.service_expire_guide_lunatalk_mobile_no">
                        <!-- <span><h5 style="margin-top:10px;">*기본값은 최초 가입시 정보로 표현됩니다.</h5></span> -->
                      </td>
                    </tr>
                    <tr>
                      <th>만료 전 안내<span class="necessary">*</span></th>
                      <td colspan="5">
                        서비스 만료
                        <select v-model="resultData.service_expire_guide_day" style="width: 90px;">
                          <option value=0>사용안함</option>
                          <option value=1>1일전</option>
                          <option value=2>2일전</option>
                          <option value=3>3일전</option>
                          <option value=4>4일전</option>
                          <option value=5>5일전</option>
                          <option value=6>6일전</option>
                          <option value=7>7일전</option>
                        </select>
                        <span style="padding: 0px 10px"> <h6>잔여기간 {{resultData.service_residual_day}} 일</h6> </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="serviceEndNotiMail"
                                 v-model="resultData.service_expire_guide_mail_use_yn"
                                 :true-value=1
                                 :false-value=0 >
                          <label for="serviceEndNotiMail">메일</label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="serviceEndNotiTalk"
                                 v-model="resultData.service_expire_guide_lunatalk_use_yn"
                                 :true-value=1
                                 :false-value=0 >
                          <label for="serviceEndNotiTalk">알림톡</label>
                        </span>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="6" class="textRight">
                        <input class="btn_delete" type="button" @click="funcReloadCancel(3)" value="취소">
                        <input class="btn_delete" type="button" @click="funcReloadSetting(3)" value="적용">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </article>
    </div>
    <v-dialog />
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import axios from 'axios'
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/src/locale'
import ModalAlert from '@/components/common/modal/ModalAlert.vue'
import PopReloadServiceHistory from '@/components/serviceManage/reload/PopReloadServiceHistory.vue'// 이력보기
import PopReloadProductDetail from '@/components/serviceManage/reload/PopReloadProductDetail.vue'// 미리보기
import { mapActions } from 'vuex'

export default {
  name: 'ReloadDetail',
  data () {
    return {
      languages: lang,
      isLoading: false,
      // searchMallData: {}, // 현재 화면을 조회했던 search param
      resultOrgData: {},
      resultData: {
        service_stat_code: 1,
        reload_key: '',
        service_expire_guide_mail_use_yn: 0,
        service_expire_guide_lunatalk_use_yn: 0,
        product_image_type_code: 1,
        product_price_type_code: 1,
        product_buy_switch_day: 15,
        service_residual_day: 0,
        service_expire_guide_day: 0,
        service_first_date: new Date(),
        service_start_date: new Date(),
        service_end_date: new Date()
      },
      firstSettingStartDate: new Date(),
      isFirstYn: false, // 최초등록여부
      freePeriodYn: false,
      historyMemoCode: 1,
      historyMemo: '',
      serviceDay: 0,
      productCategoryInfoData: '',
      categoryInfoDataMsg: '카테고리정보를 불러오고 있습니다.',
      selectCategoryNo: '',

      reloadHttp: axios.create({
        // eslint-disable-next-line no-undef
        baseURL: reloadApiUrl,
        headers: {'Content-type': 'application/json', Authorization: 'Api-Key N1ItieUV.a20YAZe3ekIOBKowWZdPsK5izg929ZQ0'}})
    }
  },
  created () {
    // 새로고침
    if (this.propSearchMallData !== undefined) {
      let storeProps = {}
      storeProps.mallSearchInfo = JSON.parse(JSON.stringify(this.propSearchMallData))
      this.routeProps(storeProps) // 처음 조회시 store 에 저장
    } else {
      this.propSearchMallData = this.$store.getters.getRouteProps.mallSearchInfo
    }

    this.get()
    this.funcProductCategory()
  },
  mounted () {
  },
  watch: {
    serviceDay (newVal) {
      let date = new Date(this.resultData.service_start_date)
      this.resultData.service_end_date = date.setDate(date.getDate() + Number(newVal) - 1)// 시작일을 포함해야 하기때문에 실제 증가일보다 1 차감한 날을 적용
    },
    firstSettingStartDate (newVal) {
      let toDay = Number(new Date())
      let date = new Date(newVal)
      let selectDay = Number(date)

      // 오늘이전 선택 불가처리
      if (selectDay < toDay) {
        this.firstSettingStartDate = new Date()
      }

      this.resultData.service_start_date = new Date(date)
      this.resultData.service_end_date = new Date(date.setDate(date.getDate() + 13))
    },
    resultData: {
      deep: true,
      handler: function (obj) {
        let toDay = Number(new Date())
        let selectDay = Number(obj.service_start_date)
        if (selectDay < toDay) {
          obj.service_start_date = new Date()
        }
      }
    }
  },
  methods: {
    ...mapActions(['routeProps']),
    get () {
      let memId = this.propSearchMallData.member_id
      let shopId = this.propSearchMallData.member_mall_no
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId, {
        })
        .then(response => {
          if (response.data.data.mall_id !== undefined) { // 수정의 경우
            this.resultData = response.data.data
            this.firstSettingStartDate = new Date(this.resultData.service_start_date)
            this.resultOrgData = Object.assign({}, response.data.data)
            this.isFirstYn = false
          } else { // 최초 등록의 경우
            let endDate = this.resultData.service_end_date
            this.resultData.service_end_date = endDate.setDate(endDate.getDate() + 13)
            this.resultData.service_first_date = new Date(this.resultData.service_start_date)
            this.resultOrgData = Object.assign({}, this.resultData)
            this.isFirstYn = true
          }
        })
        .catch(e => {
          console.log(e)
        })
    },
    /**
     * 리로드 서비스 시작
     */
    funcReloadServiceStart () {
      if (!this.userMenuAuth.create_data) {
        return false
      } else if (this.selectCategoryNo === '') {
        // 리로드 서비스 시작시 상품연동을 함께 처리하기 위해 카테고리선택 필수
        this.$modal.show(ModalAlert,
          {title: '리로드 서비스 시작', text: '상품 연동을 위한 상품카테고리 정보를 선택해주세요.'
          }, {width: 450, height: 'auto'})
        return false
      } else if (this.resultData.reload_key === '') {
        this.$modal.show(ModalAlert,
          {title: '리로드 서비스 시작', text: '리로드 KEY를 생성해주세요.'
          }, {width: 450, height: 'auto'})
        return false
      } else if (!this.requireCheck(1)) {
        return false
      }

      let memId = this.propSearchMallData.member_id
      let shopId = this.propSearchMallData.member_mall_no
      let msg = '리로스 서비스가 시작되었습니다.'
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId + '/register_service_pangx2')
        .then(response => {
          if (response.data.task_status_code === '') {
            this.funcProductRelSetting()// 상품연동
            this.resultData.service_stat_code = 2// 서비스 시작 후 [서비스사용상태] 사용중 으로 변경
            this.funcReloadSetting(1)
          } else {
            msg = response.data.task_status_message
          }
          this.$modal.show(ModalAlert,
            {
              title: '리로드 서비스 시작',
              text: msg
            }, {
              width: 450,
              height: 'auto'
            }
          )
        })
        .catch(e => {
          console.log(e)
        })
    },
    /**
     * 리로드 서비스 세팅정보 저장
     */
    funcReloadSetting (mode) {
      if (!this.userMenuAuth.modify_data && !this.userMenuAuth.create_data) {
        return false
      }
      if (!this.requireCheck(mode)) {
        return false
      }

      let param = {}
      let reloadInfo = this.resultData
      let reloadOrgInfo = this.resultOrgData

      if (mode === 1) { // 서비스 오픈 설정, 상품 연동 정보
        param = {
          mall_id: this.propSearchMallData.mall_id,
          service_stat_code: reloadInfo.service_stat_code,
          reload_key: reloadInfo.reload_key,
          // eslint-disable-next-line no-undef
          service_start_date: dateTimeFormatToString(reloadInfo.service_start_date),
          // eslint-disable-next-line no-undef
          service_end_date: dateTimeFormatToString(reloadInfo.service_end_date),
          // eslint-disable-next-line no-undef
          service_first_date: this.isFirstYn ? dateTimeFormatToString(this.firstSettingStartDate) : dateTimeFormatToString(reloadInfo.service_first_date),
          product_buy_switch_day: reloadInfo.product_buy_switch_day,
          reload_url_addr: reloadInfo.reload_url_addr,
          reload_mobile_url_addr: reloadInfo.reload_mobile_url_addr,
          product_image_type_code: reloadInfo.product_image_type_code,
          product_price_type_code: reloadInfo.product_price_type_code,
          product_url_addr: reloadInfo.product_url_addr,

          service_expire_guide_mail_use_yn: reloadOrgInfo.service_expire_guide_mail_use_yn,
          service_expire_guide_lunatalk_use_yn: reloadOrgInfo.service_expire_guide_lunatalk_use_yn,

          history_type_code: 0,
          memo_text: '', // 추후 null 처리 확인해야됨 to 니꼬르

          service_extend_yn: 0,
          user_code: 1,
          user_no: this.$store.getters.getUser.luna_no
        }
      } else if (mode === 2) { // 사용 기간 설정
        if (!this.chkServiceStart()) return false
        param = {
          mall_id: this.propSearchMallData.mall_id,
          // eslint-disable-next-line no-undef
          service_start_date: dateTimeFormatToString(reloadInfo.service_start_date),
          // eslint-disable-next-line no-undef
          service_end_date: dateTimeFormatToString(reloadInfo.service_end_date),
          history_type_code: this.historyMemoCode,
          memo_text: this.historyMemo,

          service_extend_yn: 1, // 팡팡 기간연장 api전송을 위한 flag 값
          user_code: 1,
          user_no: this.$store.getters.getUser.luna_no
        }
      } else if (mode === 3) { // 기타 설정
        if (!this.chkServiceStart()) return false
        param = {
          mall_id: this.propSearchMallData.mall_id,

          service_expire_guide_mail_use_yn: reloadInfo.service_expire_guide_mail_use_yn,
          service_expire_guide_email_addr: reloadInfo.service_expire_guide_email_addr,
          service_expire_guide_lunatalk_use_yn: reloadInfo.service_expire_guide_lunatalk_use_yn,
          service_expire_guide_lunatalk_mobile_no: reloadInfo.service_expire_guide_lunatalk_mobile_no,
          service_expire_guide_day: Number(reloadInfo.service_expire_guide_day),

          history_type_code: 0,

          service_extend_yn: 0,
          user_code: 1,
          user_no: this.$store.getters.getUser.luna_no
        }
      }

      this.reloadHttp
        .post('/api/v1/shop', param)
        .then(response => {
          console.log('오픈설정 결과: ' + response)
          let msg = '오픈 설정이 처리되었습니다.'
          if (response.data.task_status_code !== '') {
            if (response.data.task_status_code === '404_0300') {
              msg = response.data.task_status_message
            } else {
              msg = '처리중 문제가 발생 했습니다.'
            }
          }
          this.$modal.show(ModalAlert,
            {
              title: '리로드 오픈 세팅',
              text: msg
            }, {
              width: 450,
              height: 'auto'
            }
          )
          this.get()
        })
        .catch(e => {
          console.log(e)
        })
    },
    /**
     * 리로드 서비스 세팅정보 수정내용 취소
     */
    funcReloadCancel (mode) {
      let newData = this.resultData
      let orgData = this.resultOrgData
      if (mode === 2) {
        this.serviceDay = 0
        newData.service_start_date = orgData.service_start_date
        newData.service_end_date = orgData.service_end_date
        this.historyMemoCode = 1
        this.historyMemo = ''
      } else if (mode === 3) {
        newData.service_expire_guide_mail_use_yn = orgData.service_expire_guide_mail_use_yn
        newData.service_expire_guide_email_addr = orgData.service_expire_guide_email_addr
        newData.service_expire_guide_lunatalk_use_yn = orgData.service_expire_guide_lunatalk_use_yn
        newData.service_expire_guide_lunatalk_mobile_no = orgData.service_expire_guide_lunatalk_mobile_no
        newData.service_expire_guide_day = orgData.service_expire_guide_day
      }
    },
    /**
     * 이력보기 팝업
     */
    funcServiceHistory () {
      this.$modal.show(
        PopReloadServiceHistory,
        {
          data: this.propSearchMallData,
          reloadHttp: this.reloadHttp
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
    /**
     * 리로드 키 생성요청
     */
    funcKeySetting () {
      if (!this.userMenuAuth.create_data && this.resultData.reload_key !== '') {
        return false
      }
      let memId = this.propSearchMallData.member_id
      let shopId = this.propSearchMallData.member_mall_no
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId + '/generate_key')
        .then(response => {
          if (response.data.status_code === 200) {
            this.resultData.reload_key = response.data.data
          } else {
            this.$modal.show(ModalAlert,
              {
                title: '리로드 KEY 생성',
                text: '리로드 KEY 생성에 실패했습니다.'
              }, {
                width: 450,
                height: 'auto'
              }
            )
          }
        })
        .catch(e => {
          console.log(e)
        })
    },
    /**
     * 상품연동
     */
    funcProductRelSetting () {
      let memId = this.propSearchMallData.member_id
      let shopId = this.propSearchMallData.member_mall_no
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId + '/category/' + this.selectCategoryNo + '/products')
        .then(response => {
          console.log('상품연동 결과: ' + response)
          let msg = '상품연동이 처리되었습니다.'
          if (response.data.status_code !== 200) {
            msg = '처리중 문제가 발생 했습니다.'
          }
          this.$modal.show(ModalAlert,
            {
              title: '리로드 상품연동',
              text: msg
            }, {
              width: 450,
              height: 'auto'
            }
          )
          this.get()
        })
        .catch(e => {
          console.log(e)
        })
    },
    /**
     * 쇼핑몰 카테고리 정보 확인
     */
    funcProductCategory () {
      let memId = this.propSearchMallData.member_id
      let shopId = this.propSearchMallData.member_mall_no
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId + '/categories')
        .then(response => {
          if (response.data.data[0].mall_id) {
            this.productCategoryInfoData = response.data.data
          } else {
            this.categoryInfoDataMsg = '카테고리 정보가 없습니다.'
          }
        })
        .catch(e => {
          console.log(e)
        })
    },
    /**
     * 상품카테고리 연동 미리보기
     */
    funcProductRelInfo () {
      if (this.selectCategoryNo === '') {
        // 리로드 서비스 시작시 상품연동을 함께 처리하기 위해 카테고리선택 필수
        this.$modal.show(ModalAlert,
          {
            title: '상품카테고리 연동 미리보기',
            text: '상품카테고리 정보를 선택해주세요.'
          }, {
            height: 'auto'
          }
        )
        return false
      }
      this.$modal.show(
        PopReloadProductDetail,
        {
          data: {
            member_id: this.propSearchMallData.member_id,
            member_mall_no: this.propSearchMallData.member_mall_no,
            category: this.selectCategoryNo
          },
          reloadHttp: this.reloadHttp
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
    funcServiceDateAdd (cnt) {
      this.serviceDay = Number(this.serviceDay) + Number(cnt)
    },
    chkServiceStart () {
      if (this.isFirstYn) {
        this.$modal.show(ModalAlert,
          {
            title: '리로드 서비스 세팅',
            text: '서비스시작 이후 적용가능합니다.'
          }, {
            width: 450,
            height: 'auto'
          }
        )
        return false
      }
      return true
    },
    /**
     * 저장 전 필수입력 체크
     */
    requireCheck (mode) {
      let checkRes = true
      if (mode === 1) {
        if (
          this.resultData.reload_key === '' || this.resultData.reload_key === null ||
          this.resultData.product_buy_switch_day === '' || this.resultData.product_buy_switch_day === null ||
          this.resultData.reload_url_addr === '' || this.resultData.reload_url_addr === null ||
          this.resultData.reload_mobile_url_addr === '' || this.resultData.reload_mobile_url_addr === null ||
          this.resultData.product_url_addr === '' || this.resultData.product_url_addr === null ||
          this.resultData.product_image_type_code === 0 ||
          this.resultData.product_price_type_code === 0
        ) {
          checkRes = false
        }
      } else if (mode === 2) {
      } else if (mode === 3) {
        if (
          this.resultData.service_expire_guide_email_addr === '' || this.resultData.service_expire_guide_email_addr === null ||
          this.resultData.service_expire_guide_lunatalk_mobile_no === '' || this.resultData.service_expire_guide_lunatalk_mobile_no === null ||
          this.resultData.service_expire_guide_day === null
        ) {
          checkRes = false
        }
      }

      if (!checkRes) {
        this.$modal.show(ModalAlert,
          {
            title: '리로드 서비스 세팅',
            text: '필수 입력사항을 확인해주세요.'
          }, {
            width: 450,
            height: 'auto'
          }
        )
      }
      return checkRes
    }
  },
  components: {
    'Datepicker': Datepicker
  },
  props: {
    'propSettingData': Object,
    'propSearchMallData': Object,
    'codeData': Object,
    'userMenuAuth': Object
  }
}

</script>

<style scoped>
 input[type="number"] {
   border: 1px solid #e8e8e8;
 }

 .datepicker {
    display: inline-block;
    width: 110px;
 }
 .datepicker >>> input[type="text"] {
   width: 100%;
 }
</style>
