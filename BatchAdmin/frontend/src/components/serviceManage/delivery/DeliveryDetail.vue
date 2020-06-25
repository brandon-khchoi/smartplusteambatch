<template>
  <div class="content service setting option ">
    <h1>택배회수 서비스 세팅 > {{searchMallData.biz_name}} ({{searchMallData.mall_name}})</h1>
    <div class="formContainer">
      <article v-if="isLoaded">
        <div class="articleWrap">
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">택배사 정보</p>
            </div>
            <div class="detailContent">
              <div class="tableWrap">
                <table summary="택배사 정보">
                  <caption class="blind">택배사 정보</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:calc(50% - 100px)">
                    <col style="width:200px">
                    <col>
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>승인번호<span class="necessary">*</span></th>
                      <td>
                        <input type="text"  v-model="settingData.defaultInfo.delivery_company_auth_no"
                          @input="typing($event, 'no', 'defaultInfo', 'delivery_company_auth_no')"
                          @blur="lenCheck($event, 2, 'delivery_company_auth_no')" maxlength="50" />
                      </td>
                      <th>고객번호<span class="necessary">*</span></th>
                      <td>
                        <input type="text" v-model="settingData.defaultInfo.delivery_company_mall_no"
                          @input="typing($event, 'no', 'defaultInfo', 'delivery_company_mall_no')"
                          @blur="lenCheck($event, 2, 'delivery_company_mall_no')" maxlength="50"/>
                      </td>
                    </tr>
                    <tr>
                      <th>계약지점<span class="necessary">*</span></th>
                      <td>
                        <input type="text" v-model="settingData.defaultInfo.delivery_company_branch_name"
                          @blur="lenCheck($event, 2, 'delivery_company_branch_name')" maxlength="50"/>
                      </td>
                      <th>점포코드<span class="necessary">*</span></th>
                      <td>
                        <input type="text" v-model="settingData.defaultInfo.mall_delivery_code"
                          @input="typing($event, 'no', 'defaultInfo', 'mall_delivery_code')"
                          @blur="lenCheck($event, 2, 'mall_delivery_code')" maxlength="50"/>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="4" class="textRight">
                        <input class="btn_delete" type="button" value="취소" @click="cancelDialog('defaultInfo')">
                        <input class="btn_delete" type="button" value="적용" @click="confirmDialog('defaultInfo')">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">반품처 정보</p>
            </div>
            <div class="detailContent">
              <div class="tableWrap">
                <table summary="반품처 정보">
                  <caption class="blind">반품처 정보</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:calc(50% - 100px)">
                    <col style="width:200px">
                    <col>
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>반품처명<span class="necessary">*</span></th>
                      <td colspan="3">
                        <input type="text"  v-model="settingData.returnPlaceInfo.return_place_name"
                          @blur="lenCheck($event, 2, 'return_place_name')" maxlength="50" />
                      </td>
                    </tr>
                    <tr>
                      <th>주소<span class="necessary">*</span></th>
                      <td colspan="3">
                        <p class="addressWrap tableRegister ">
                          <input type="text" style="width:150px;" class="noinputBox" v-model="settingData.returnPlaceInfo.return_place_zipcode" readonly />
                          <input class="btn_save" type="button" value="주소찾기"
                                  color="blue"
                                  size="s"
                                  @click="() => openPostcode()"
                          />
                          <input type="text" v-model="settingData.returnPlaceInfo.return_place_addr" readonly/>
                          <input type="text"
                                  v-model="settingData.returnPlaceInfo.return_place_detail_addr"
                                  @blur="lenCheck($event, 2, 'return_place_detail_addr')"
                                  maxlength="100"
                          />
                          <postcode
                              :opened="isOpenedPostcode"
                              @close="() => isOpenedPostcode = false"
                              @complete="result => setAddress(result)"
                          />
                        </p>
                      </td>
                    </tr>
                    <tr>
                      <th>연락처1<span class="necessary">*</span></th>
                      <td>
                        <p class="phoneNumWrap">
                          <input type="text"
                                  maxlength="3"
                                  v-model="settingData.returnPlaceInfo.return_place_tel_no[0]"
                                  @blur="lenCheck($event, 2, 'return_place_tel_no1')"
                                  @input="typing($event, 'no', 'returnPlaceInfo','return_place_tel_no', 0)"
                          />
                          <span></span>
                          <input type="text"
                                  maxlength="4"
                                  v-model="settingData.returnPlaceInfo.return_place_tel_no[1]"
                                  @blur="lenCheck($event, 3, 'return_place_tel_no2')"
                                  @input="typing($event, 'no', 'returnPlaceInfo', 'return_place_tel_no', 1)"
                          />
                          <span></span>
                          <input type="text"
                                  maxlength="4"
                                  v-model="settingData.returnPlaceInfo.return_place_tel_no[2]"
                                  @blur="lenCheck($event, 3, 'return_place_tel_no3')"
                                  @input="typing($event, 'no', 'returnPlaceInfo', 'return_place_tel_no', 2)"
                          />
                        </p>
                      </td>
                      <th>연락처2</th>
                      <td>
                        <p class="phoneNumWrap">
                          <input type="text"
                                  maxlength="3"
                                  v-model="settingData.returnPlaceInfo.return_place_tel_sub_no[0]"
                                  @blur="lenCheck($event, 2, 'return_place_tel_sub_no1')"
                                  @input="typing($event, 'no', 'returnPlaceInfo', 'return_place_tel_sub_no', 0)"
                          />
                          <span></span>
                          <input type="text"
                                  maxlength="4"
                                  v-model="settingData.returnPlaceInfo.return_place_tel_sub_no[1]"
                                  @blur="lenCheck($event, 3, 'return_place_tel_sub_no2')"
                                  @input="typing($event, 'no', 'returnPlaceInfo', 'return_place_tel_sub_no', 1)"
                          />
                          <span></span>
                          <input type="text"
                                  maxlength="4"
                                  v-model="settingData.returnPlaceInfo.return_place_tel_sub_no[2]"
                                  @blur="lenCheck($event, 3, 'return_place_tel_sub_no3')"
                                  @input="typing($event, 'no', 'returnPlaceInfo', 'return_place_tel_sub_no', 2)"
                          />
                        </p>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="4" class="textRight">
                        <input class="btn_delete" type="button" value="취소" @click="cancelDialog('returnPlaceInfo')">
                        <input class="btn_delete" type="button" value="적용" @click="confirmDialog('returnPlaceInfo')">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">택배비 설정</p>
            </div>
            <div class="detailContent">
              <div class="tableWrap">
                <table summary="택배사 설정">
                  <caption class="blind">택배사 설정</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:calc(50% - 100px)">
                    <col style="width:200px">
                    <col>
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>초기택배비<span class="necessary">*</span></th>
                      <td>
                        <input type="text" v-model="settingData.deliveryPriceSetting.delivery_default_price"
                          @input="typing($event, 'money', 'deliveryPriceSetting','delivery_default_price')"
                          maxlength="10" />
                      </td>
                      <th>무료배송비 기준<span class="necessary">*</span></th>
                      <td>
                        <input type="text" v-model="settingData.deliveryPriceSetting.delivery_free_order_price"
                         @input="typing($event, 'money', 'deliveryPriceSetting', 'delivery_free_order_price')"
                         maxlength="10"/>
                      </td>
                    </tr>
                    <tr>
                      <th>교환반품 접수 가능일<span class="necessary">*</span></th>
                      <td>
                        <input type="text" v-model="settingData.deliveryPriceSetting.order_return_day_no" style="width:100px"
                        @input="typing($event, 'money', 'deliveryPriceSetting','order_return_day_no')"
                        @blur="lenCheck($event, 1, 'order_return_day_no')" maxlength="10"/><span>일 까지</span>
                        <p class="btn_min btn_setting popupBtn"  @click="showBasicModal" >기준</p>
                      </td>
                      <th>상태 자동 변경<span class="necessary">*</span></th>
                      <td>
                        <span>직접 회수 접수 건은</span>
                        <input type="text" v-model="settingData.deliveryPriceSetting.order_return_stat_change_day"
                         @input="typing($event, 'money', 'deliveryPriceSetting','order_return_stat_change_day')"
                         @blur="lenCheck($event, 1, 'order_return_stat_change_day')" maxlength="10" style="width:100px"/>
                        <span>일 이후 완료대기로 자동변경</span>
                      </td>
                    </tr>
                    <tr>
                      <th>환불수단</th>
                      <td colspan="3">
                      <span class="inputWrap">
                        <input type="checkbox" id="service01-01" :true-value="1" :false-value="0" v-model="settingData.deliveryPriceSetting.saving_yn">
                        <label for="service01-01">적립금</label>
                      </span>
                      <span class="inputWrap">
                        <input type="checkbox" id="service01-02" :true-value="1" :false-value="0" v-model="settingData.deliveryPriceSetting.deposit_yn">
                        <label for="service01-02">예치금</label>
                      </span>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="4" class="textRight">
                        <input class="btn_delete" type="button" value="취소" @click="cancelDialog('deliveryPriceSetting')">
                        <input class="btn_delete" type="button" value="적용" @click="confirmDialog('deliveryPriceSetting')">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">회수택배비 설정</p>
            </div>
            <template v-for="item in settingData.returnPriceSetting">
              <DeliveryBasicSettingList :key="item.id" :title="item.reason_name" :seg="item.reason_no" :dataList="item.requestInfos"
                :defaultPrice="initSettingData.deliveryPriceSetting.delivery_default_price"
              ></DeliveryBasicSettingList>
            </template>

          </div>

          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">안내 문구 등록</p>
            </div>
            <div class="detailContent">
              <div class="serviceGuideField">
                <div class="innerWrapper">
                  <div class="guideRegistration">
                    <table summary="안내 문구 등록">
                      <caption class="blind">안내 문구 등록</caption>
                      <colgroup>
                        <col style="width:160px">
                        <col>
                      </colgroup>
                      <tbody>
                        <tr>
                          <th>교환반품 불가 안내<span class="necessary">*</span></th>
                          <td>
                            <div class="textareaWrap">
                              <textarea rows="5" v-model="settingData.guideText.order_return_deny_guide_text" maxlength="1000"
                              @click="changeImg('order_return_deny_guide_text')"
                              ></textarea>
                              <p>{{order_return_deny_guide_textLen}} /1000</p>
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <th>교환반품 불가 문구<span class="necessary">*</span></th>
                          <td>
                            <div class="textareaWrap">
                              <textarea rows="5"  v-model="settingData.guideText.order_return_guide_text" maxlength="500"
                              @click="changeImg('order_return_guide_text')"
                              ></textarea>
                              <p>{{order_return_guide_textLen }} /500</p>
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <th>회수신청 안내 문구<span class="necessary">*</span></th>
                          <td>
                            <div class="textareaWrap">
                              <textarea rows="5"  v-model="settingData.guideText.request_guide_text" maxlength="500"
                              @click="changeImg('request_guide_text')"
                               ></textarea>
                              <p>{{request_guide_textLen }} /500</p>
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <th>결제정보 안내 문구<span class="necessary">*</span></th>
                          <td>
                            <div class="textareaWrap">
                              <textarea rows="5"  v-model="settingData.guideText.pay_guide_text" maxlength="500"
                              @click="changeImg('pay_guide_text')"
                              ></textarea>
                              <p>{{pay_guide_textLen}} /500</p>
                            </div>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                    <div class="btnWrap">
                      <ul>
                        <li><input class="btn_cancel" type="button" value="취소" @click="cancelDialog('guideText')"></li>
                        <li><input class="btn_save" type="button" value="적용" @click="confirmDialog('guideText')"></li>
                      </ul>
                    </div>
                  </div>
                  <div class="mobileScreen">
                    <img src="//static.lunasoft.co.kr/lunaadmin/images/serviceSettinGuide_1.png" class= "guideTxtImg" id="order_return_deny_guide_textImg" alt="교환반품 불가 안내">
                    <img src="//static.lunasoft.co.kr/lunaadmin/images/serviceSettinGuide_2.png" class= "guideTxtImg" id="order_return_guide_textImg" alt="교환반품 불가 문구" style="display: none;">
                    <img src="//static.lunasoft.co.kr/lunaadmin/images/serviceSettinGuide_3.png" class= "guideTxtImg" id="request_guide_textImg" alt="회수신청 안내 문구" style="display: none;">
                    <img src="//static.lunasoft.co.kr/lunaadmin/images/serviceSettinGuide_4.png" class= "guideTxtImg" id="pay_guide_textImg" alt="결제정보 안내 문구" style="display: none;">
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="settingDetailWrap">
            <div class="titleWrap">
              <p class="title">택배회수 오픈 설정</p>
              <p class="toggleWrap" style="margin-left: 10px;margin-top: 5px;">
                <label class="switch">
                  <template v-if="initSettingData.openSetting.service_stat_code == 5">
                    <input type="checkbox" :true-value="1" :false-value="0" v-model="openSettingUseYn"/>
                  </template>
                  <template>
                    <input type="checkbox" :true-value="1" :false-value="0" v-model="openSettingUseYn" disabled/>
                  </template>
                  <span class="slider"></span>
                </label>
              </p>
            </div>
            <div class="detailContent">
              <div class="tableWrap">
                <table summary="택배회수">
                  <caption class="blind">택배회수</caption>
                  <colgroup>
                    <col style="width:200px">
                    <col style="width:calc(50% - 100px)">
                    <col style="width:100px">
                    <col>
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>루나플러스 메뉴 노출</th>
                      <td>
                        <p class="toggleWrap">
                          <label class="switch">
                            <input v-if="openSettingUseYn === 0" type="checkbox" :true-value="1" :false-value="0" v-model="settingData.openSetting.display_lunaplus_menu_yn" disabled >
                            <input v-else type="checkbox" :true-value="1" :false-value="0" v-model="settingData.openSetting.display_lunaplus_menu_yn">
                            <span class="slider"></span>
                          </label>
                        </p>

                      </td>
                      <th>택배사<span class="necessary">*</span></th>
                      <td>
                        {{searchMallData.selectedDelivertInfo.delivery_company_name}}
                      </td>

                    <tr>
                      <th>서비스 사용 상태<span class="necessary">*</span></th>
                      <td>
                        <template v-if="initSettingData.openSetting.service_stat_code == 5">
                          <select v-if="openSettingUseYn === 0" disabled v-model="settingData.openSetting.service_stat_code" @change="dateTimeParse">
                              <option :value="5">선택하세요</option>
                              <option :value="1">오픈 예정</option>
                          </select>
                          <select v-else v-model="settingData.openSetting.service_stat_code" @change="dateTimeParse">
                              <option :value="5">선택하세요</option>
                              <option :value="1">오픈 예정</option>
                          </select>
                        </template>
                        <template v-else>
                          <select v-model="settingData.openSetting.service_stat_code" @change="dateTimeParse">
                          <template v-for="(item , idx ) in codeData.serviceStatCodeOrderData" >
                            <option :value="item[idx + 1].comm_code" :key="item.id">
                              {{item[idx + 1].comm_code_kor_name }}
                            </option>
                          </template>
                          </select>
                        </template>
                      </td>
                      <th>사용 시작일시</th>
                      <td>
                        <div class="calendarWrapper">
                          <template v-if="settingData.openSetting.service_stat_code == 1 || settingData.openSetting.service_stat_code == 4">
                            <SingleDatepicker
                             :propDate="startDate.date"
                             v-on:update:startDate="setStartDate"
                            />
                            <select style="width:80px" v-model="startDate.hour">
                              <option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option><option value="04">04</option>
                              <option value="05">05</option><option value="06">06</option><option value="07">07</option><option value="08">08</option><option value="09">09</option>
                              <option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="13">13</option>
                              <option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option>
                              <option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option>
                            </select>
                            <select style="width:80px" v-model="startDate.time">
                              <option value="00">00</option>
                              <option value="30">30</option>
                            </select>
                          </template>
                          <template v-else>

                            <input
                              type="text"
                              placeholder="예) 2019-07-27"
                              v-model="startDate.date"
                              disabled
                            >
                            <img class="ui-datepicker-trigger" src="//static.lunasoft.co.kr/lunaadmin/images/calendar.png" style="margin-left:-5px" >
                            <select style="width:80px" v-model="startDate.hour" disabled>
                              <option :value="startDate.hour">{{startDate.hour}}</option>
                              </select>
                            <select style="width:80px" v-model="startDate.time" disabled>
                              <option :value="startDate.time">{{startDate.time}}</option>
                            </select>
                          </template>
                          <p class="btn_min btn_setting popupBtn" @click="showHistory" >사용 이력</p>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="4" class="textRight">
                        <input class="btn_delete" type="button" value="취소" @click="cancelDialog('openSetting')">
                        <input class="btn_delete" type="button" value="적용" @click="confirmDialog('openSetting')">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
        <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
      </article>
    </div>
    <v-dialog />
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>

import SingleDatepicker from '@/components/common/SingleDatepicker'
import DeliveryBasicSettingList from '@/components/serviceManage/delivery/DeliveryBasicSettingList'
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalListAlert from '@/components/common/modal/ModalListAlert'
import DeliveryModalHistory from '@/components/serviceManage/delivery/DeliveryModalHistory'
import Postcode from '@/components/common/Postcode'
import { mapActions } from 'vuex'

export default {
  name: 'DeliveryDetail',
  components: {
    DeliveryBasicSettingList,
    DeliveryModalHistory,
    SingleDatepicker,
    Postcode
  },
  data () {
    return {
      settingData: {}, // 화면 데이터
      initSettingData: {}, // 초기화용 데이터
      searchMallData: {}, // 현재 화면을 조회했던 search param
      openSettingUseYn: 0,
      isModalOpen: false,
      startDate: {
        date: '',
        hour: '',
        time: ''
      },
      isLoading: false,
      isLoaded: true, // vue bind 때문에 필요
      userNo: this.$store.getters.getUser.luna_no,
      isOpenedPostcode: false,
      isChangedData: { // 데이터를 변경했는지 flag
        isDefaultInfoChanged: false, // 데이터가 변경되었을 경우 true, 취소및 적용시 false로 초기화
        isReturnPlaceInfoChanged: false,
        isDeliveryPriceSettingChanged: false,
        isGuideTextChanged: false,
        isReturnPriceSettingChanged: false
      },
      isResetData: { // 변경된 데이터를 reset 했는지 flag
        isResetDefaultInfo: false,
        isResetReturnPlaceInfo: false,
        isResetDeliveryPriceSetting: false,
        isResetGuideText: false,
        isResetReturnPriceSetting: false
      },
      valiAlert: {
        defaultInfo: {
          delivery_company_auth_no: '[택배사 정보] 승인번호를 입력해주세요.',
          delivery_company_branch_name: '[택배사 정보] 계약지점을 입력해주세요.',
          delivery_company_mall_no: '[택배사 정보] 고객번호를 입력해주세요.',
          mall_delivery_code: '[택배사 정보] 점포코드를 입력해주세요.'
        },
        returnPlaceInfo: {
          return_place_name: '[반품처 정보] 반품처명을 입력해주세요.',
          return_place_zipcode: '[반품처 정보] 우편번호를 입력해주세요.',
          return_place_addr: '[반품처 정보] 기본주소를 입력해주세요.',
          return_place_detail_addr: '[반품처 정보] 상세주소를 입력해주세요.',
          return_place_tel_no: '[반품처 정보] 연락처1을 입력해주세요.',
          return_place_tel_sub_no: '[반품처 정보] 연락처2을 자리수에 맞춰 입력해주세요.'
        },
        deliveryPriceSetting: {
          delivery_default_price: '[택배비 설정] 초기택배비를 입력해주세요.',
          delivery_free_order_price: '[택배비 설정] 무료배송비 기준을 입력해주세요.',
          order_return_day_no: '[택배비 설정] 교환반품 접수 가능일을 입력해주세요.',
          order_return_stat_change_day: '[택배비 설정] 상태 자동 변경일을 입력해주세요.'
        },
        guideText: {
          order_return_deny_guide_text: '[안내 문구 등록] 교환반품 불가 안내 문구를 입력해주세요.',
          order_return_guide_text: '[안내 문구 등록] 교환반품 불가 문구를 입력해주세요.',
          pay_guide_text: '[안내 문구 등록] 결제정보 안내 문구를 입력해주세요.',
          request_guide_text: '[안내 문구 등록] 회수신청 안내 문구를 입력해주세요.'
        },
        openSetting: {
          date: '[택배회수 오픈 설정] 사용 시작일을 정해주세요.',
          hour: '[택배회수 오픈 설정] 사용 시작시간을 정해주세요.',
          time: '[택배회수 오픈 설정] 사용 시작시간을 정해주세요.'
        },
        finalInfoSet: {
          isDefaultInfoChanged: '[택배사 정보] 설정한 정보를 적용해주세요.',
          isReturnPlaceInfoChanged: '[반품처 정보] 설정한 정보를 적용해주세요.',
          isDeliveryPriceSettingChanged: '[택배비 설정] 설정한 정보를 적용해주세요.',
          isGuideTextChanged: '[안내 문구 등록] 설정한 정보를 적용해주세요.',
          isReturnPriceSettingChanged: '[회수택배비 설정] 설정한 정보를 적용해주세요.'
        }
      }
    }
  },
  created () {
    // console.log('created')

    if (this.propSettingData !== undefined) {
      this.settingData = JSON.parse(JSON.stringify(this.propSettingData))
      this.initSettingData = JSON.parse(JSON.stringify(this.propSettingData))
    } else { // 새로고침 분기
      this.settingData = null
      this.isLoaded = false
    }
    if (this.propSearchMallData !== undefined) {
      this.searchMallData = JSON.parse(JSON.stringify(this.propSearchMallData))
      let storeProps = {}
      storeProps.mallSearchInfo = this.searchMallData
      this.routeProps(storeProps) // 처음 조회시 store 에 저장
    } else {
      this.searchMallData = this.$store.getters.getRouteProps.mallSearchInfo
    }

    if (this.settingData === null) { // 새로고침시 호출되어야 하는 내용
      this.getRefreshData()
      // console.log('새로고침 created')
    } else {
      this.serviceSettingCommon()
    }

    // console.log('created end')
  },
  mounted () {
    // console.log('detail mounted')
    // console.log('detail mounted finish')
  },
  watch: {
    'settingData.defaultInfo': {
      deep: true,
      handler (newVal, oldVal) {
        // console.log(newVal === oldVal)

        if (oldVal !== undefined) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          // isChangedData 를 false 로 변경시 isResetData를 true 로 변경해야 handler 사용 가능
          if (!this.isResetData.isResetDefaultInfo) { // Reset 한적 없는 data 만 값 변경
            this.isChangedData.isDefaultInfoChanged = true
            // console.log('defaultInfo has changed!')
          } else { // reset 시 다시 변경
            // console.log('defaultInfo not changed! 안변함')
            this.isResetData.isResetDefaultInfo = !this.isResetData.isResetDefaultInfo
          }
        }
      }
    },
    'settingData.returnPlaceInfo': {
      deep: true,
      handler (newVal, oldVal) {
        // console.log(newVal === oldVal)
        if (oldVal !== undefined) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          if (!this.isResetData.isResetReturnPlaceInfo) { // Reset 한적 없는 data 만 값 변경
            this.isChangedData.isReturnPlaceInfoChanged = true
          } else { // reset 시 다시 변경
            this.isResetData.isResetReturnPlaceInfo = !this.isResetData.isResetReturnPlaceInfo
          }
        }
      }
    },
    'settingData.deliveryPriceSetting': {
      deep: true,
      handler (newVal, oldVal) {
        if (oldVal !== undefined) {
          if (!this.isResetData.isResetDeliveryPriceSetting) {
            this.isChangedData.isDeliveryPriceSettingChanged = true
            // console.log('deliveryPriceSetting has changed!')
          } else {
            this.isResetData.isResetDeliveryPriceSetting = !this.isResetData.isResetDeliveryPriceSetting
          }
        }
      }
    },
    'settingData.guideText': {
      deep: true,
      handler (newVal, oldVal) {
        if (oldVal !== undefined) {
          if (!this.isResetData.isResetGuideText) {
            this.isChangedData.isGuideTextChanged = true
            // console.log('guideText has changed!')
          } else {
            this.isResetData.isResetGuideText = !this.isResetData.isResetGuideText
          }
        }
      }
    },
    'settingData.returnPriceSetting': {
      deep: true,
      handler (newVal, oldVal) {
        if (oldVal !== undefined) {
          if (!this.isResetData.isResetReturnPriceSetting) {
            this.isChangedData.isReturnPriceSettingChanged = true
            // console.log('returnPriceSetting has changed!')
          } else {
            this.isResetData.isResetReturnPriceSetting = !this.isResetData.isResetReturnPriceSetting
          }
        }
      }
    }

  },
  computed: {
    order_return_deny_guide_textLen () {
      let textLen = 0; if (this.settingData.guideText.order_return_deny_guide_text !== null) textLen = this.settingData.guideText.order_return_deny_guide_text.length
      return textLen
    },
    order_return_guide_textLen () {
      let textLen = 0; if (this.settingData.guideText.order_return_guide_text !== null) textLen = this.settingData.guideText.order_return_guide_text.length
      return textLen
    },
    pay_guide_textLen () {
      let textLen = 0; if (this.settingData.guideText.pay_guide_text !== null) textLen = this.settingData.guideText.pay_guide_text.length
      return textLen
    },
    request_guide_textLen () {
      let textLen = 0; if (this.settingData.guideText.request_guide_text !== null) textLen = this.settingData.guideText.request_guide_text.length
      return textLen
    }

  },
  methods: {
    ...mapActions(['routeProps']),
    initData (part) { // 취소 버튼으로 초기화
      // console.log(part)
      if (part === 'defaultInfo') {
        this.settingData.defaultInfo = JSON.parse(JSON.stringify(this.initSettingData.defaultInfo))
        this.isChangedData.isDefaultInfoChanged = false
        this.isResetData.isResetDefaultInfo = true
      } else if (part === 'returnPlaceInfo') {
        this.settingData.returnPlaceInfo = JSON.parse(JSON.stringify(this.initSettingData.returnPlaceInfo))
        this.isChangedData.isReturnPlaceInfoChanged = false
        this.isResetData.isResetReturnPlaceInfo = true
      } else if (part === 'deliveryPriceSetting') {
        this.settingData.deliveryPriceSetting = JSON.parse(JSON.stringify(this.initSettingData.deliveryPriceSetting))
        this.isChangedData.isDeliveryPriceSettingChanged = false
        this.isResetData.isResetDeliveryPriceSetting = true
      } else if (part === 'returnPriceSetting') {
        this.settingData.returnPriceSetting = JSON.parse(JSON.stringify(this.initSettingData.returnPriceSetting))
        this.isChangedData.isReturnPriceSettingChanged = false
        this.isResetData.isResetReturnPriceSetting = true
      } else if (part === 'guideText') {
        this.settingData.guideText = JSON.parse(JSON.stringify(this.initSettingData.guideText))
        this.isChangedData.isGuideTextChanged = false
        this.isResetData.isResetGuideText = true
      } else if (part === 'openSetting') {
        this.settingData.openSetting = JSON.parse(JSON.stringify(this.initSettingData.openSetting))
        this.dateTimeParse()
        this.serviceOpenUseYn()
      }
    },
    saveData (part) {
      // console.log('saveData : ' + part)
      let form = new FormData()
      form.append('mall_id', this.searchMallData.mall_id)

      form.append('user_code', 1)
      form.append('user_no', this.userNo)
      form.append('member_id', this.searchMallData.member_id)
      form.append('delivery_company_no', this.searchMallData.selectedDelivertInfo.delivery_company_no)
      if (part === 'defaultInfo') {
        if (!this.defaultInfoValidation()) return false

        let defaultInfo = this.settingData.defaultInfo
        form.append('delivery_company_mall_no', defaultInfo.delivery_company_mall_no); form.append('delivery_company_auth_no', defaultInfo.delivery_company_auth_no)
        form.append('delivery_company_branch_name', defaultInfo.delivery_company_branch_name); form.append('mall_delivery_code', defaultInfo.mall_delivery_code)
        // eslint-disable-next-line no-undef
        this.postData(apiServerUrl + '/serviceSetting/deliveryService/setDefaultInfo', form, part)
      } else if (part === 'returnPlaceInfo') {
        if (!this.returnPlaceInfoValidation()) return false

        let returnPlaceInfo = this.settingData.returnPlaceInfo
        form.append('return_place_name', returnPlaceInfo.return_place_name)
        form.append('return_place_tel_no', returnPlaceInfo.return_place_tel_no[0] + '-' + returnPlaceInfo.return_place_tel_no[1] + '-' + returnPlaceInfo.return_place_tel_no[2])
        if (returnPlaceInfo.return_place_tel_sub_no[0] && returnPlaceInfo.return_place_tel_sub_no[1] && returnPlaceInfo.return_place_tel_sub_no[2]) {
          form.append('return_place_tel_sub_no', returnPlaceInfo.return_place_tel_sub_no[0] + '-' + returnPlaceInfo.return_place_tel_sub_no[1] + '-' + returnPlaceInfo.return_place_tel_sub_no[2])
        }
        form.append('return_place_zipcode', returnPlaceInfo.return_place_zipcode)
        form.append('return_place_addr', returnPlaceInfo.return_place_addr); form.append('return_place_detail_addr', returnPlaceInfo.return_place_detail_addr)
        // eslint-disable-next-line no-undef
        this.postData(apiServerUrl + '/serviceSetting/deliveryService/setReturnPlaceInfo', form, part)
      } else if (part === 'deliveryPriceSetting') {
        if (!this.deliveryPriceSettingValidation()) return false

        let deliveryPriceSetting = this.settingData.deliveryPriceSetting
        form.append('DeliveryPriceSetting.delivery_default_price', deliveryPriceSetting.delivery_default_price); form.append('DeliveryPriceSetting.delivery_free_order_price', deliveryPriceSetting.delivery_free_order_price)
        form.append('DeliveryPriceSetting.order_return_day_no', deliveryPriceSetting.order_return_day_no); form.append('DeliveryPriceSetting.order_return_stat_change_day', deliveryPriceSetting.order_return_stat_change_day)
        form.append('DeliveryPriceSetting.saving_yn', deliveryPriceSetting.saving_yn); form.append('DeliveryPriceSetting.deposit_yn', deliveryPriceSetting.deposit_yn)
        // eslint-disable-next-line no-undef
        this.postData(apiServerUrl + '/serviceSetting/deliveryService/setDeliveryPriceDefaultSetting', form, part)
      } else if (part === 'returnPriceSetting') {
        let sendData = {}
        sendData.mall_id = this.searchMallData.mall_id; sendData.user_code = 1; sendData.user_no = this.userNo
        sendData.member_id = this.searchMallData.member_id; sendData.delivery_company_no = this.searchMallData.selectedDelivertInfo.delivery_company_no
        sendData.returnPriceDefaultSetting = this.settingData.returnPriceSetting
        // eslint-disable-next-line no-undef
        this.postData(apiServerUrl + '/serviceSetting/deliveryService/setReturnPriceSetting', sendData, part)
      } else if (part === 'guideText') {
        if (!this.guideTextValidation()) return false
        let guideText = this.settingData.guideText
        form.append('GuideText.order_return_guide_text', guideText.order_return_guide_text); form.append('GuideText.order_return_deny_guide_text', guideText.order_return_deny_guide_text)
        form.append('GuideText.request_guide_text', guideText.request_guide_text); form.append('GuideText.pay_guide_text', guideText.pay_guide_text)
        // eslint-disable-next-line no-undef
        this.postData(apiServerUrl + '/serviceSetting/deliveryService/setGuideText', form, part)
      } else if (part === 'openSetting') {
        if (!this.openSettingValidation()) return false
        if (!this.defaultInfoValidation()) return false
        if (!this.returnPlaceInfoValidation()) return false
        if (!this.deliveryPriceSettingValidation()) return false
        if (!this.guideTextValidation()) return false
        if (!this.finalValidation()) return false

        let openSetting = this.settingData.openSetting
        form.append('mall_no', this.searchMallData.mall_no)
        form.append('display_lunaplus_menu_yn', openSetting.display_lunaplus_menu_yn)
        form.append('service_stat_code', openSetting.service_stat_code)

        // eslint-disable-next-line eqeqeq
        if (openSetting.service_stat_code == 1 || openSetting.service_stat_code == 4) { // 오픈 예정 과 일시 정지 일때만 사용시작일시 업데이트
        // eslint-disable-next-line no-undef
          const formatDate = dateFormatToString(this.startDate.date)
          let dateParam = formatDate + ' ' + this.startDate.hour + ':' + this.startDate.time + ':00'
          form.append('service_start_date', dateParam)
        }
        // eslint-disable-next-line no-undef
        this.postData(apiServerUrl + '/serviceSetting/deliveryService/setOpenSetting', form, part)
      }
    },
    postData (url, form, part) {
      this.isLoading = true
      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          // console.log(response)
          if (response.data.resultCode === 0) {
            this.responseHandle(part)
            this.$modal.show(ModalAlert,
              { title: '', text: '적용하였습니다.' }, { width: 450, height: 'auto' }
            )
          } else {
            this.responseHandle(part)
            this.$modal.show(ModalAlert,
              { title: '', text: '시스템 오류.' }, { width: 450, height: 'auto' }
            )
          }
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
    responseHandle (part) { // 서버에서 받아온 데이터 다시 초기화 데이터로 세팅
      // console.log('responseHandle : ' + part)
      if (part === 'defaultInfo') {
        this.initSettingData.defaultInfo = JSON.parse(JSON.stringify(this.settingData.defaultInfo))
        this.isChangedData.isDefaultInfoChanged = false
      } if (part === 'returnPlaceInfo') {
        this.initSettingData.returnPlaceInfo = JSON.parse(JSON.stringify(this.settingData.returnPlaceInfo))
        this.isChangedData.isReturnPlaceInfoChanged = false
      } else if (part === 'deliveryPriceSetting') {
        this.initSettingData.deliveryPriceSetting = JSON.parse(JSON.stringify(this.settingData.deliveryPriceSetting))
        this.isChangedData.isDeliveryPriceSettingChanged = false
      } else if (part === 'returnPriceSetting') {
        this.initSettingData.returnPriceSetting = JSON.parse(JSON.stringify(this.settingData.returnPriceSetting))
        this.isChangedData.isReturnPriceSettingChanged = false
      } else if (part === 'guideText') {
        this.initSettingData.guideText = JSON.parse(JSON.stringify(this.settingData.guideText))
        this.isChangedData.isGuideTextChanged = false
      } else if (part === 'openSetting') {
        this.initSettingData.openSetting = JSON.parse(JSON.stringify(this.settingData.openSetting))
        // this.getRefreshData() // 이력보기의 경우 전체 api 에서 가져오므로 적용시 전체 데이터 재조회
      }
    },
    confirmDialog (part) {
      if (!this.userMenuAuth.modify_data) {
        return
      }
      this.$modal.show('dialog', {
        text: '적용하시겠습니까?',
        buttons: [
          { title: '취소' },
          { title: '확인',
            default: true,
            handler: () => {
              this.saveData(part)
              this.$modal.hide('dialog')
            }
          }]
      })
    },
    cancelDialog (part) {
      this.$modal.show('dialog', {
        text: '취소하시겠습니까?',
        buttons: [
          { title: '취소' },
          { title: '확인',
            default: true,
            handler: () => {
              this.initData(part)
              this.$modal.hide('dialog')
            }
          }]
      })
    },
    serviceOpenUseYn () { // 택배회수 오픈 설정 토글탭
      // 코드 5 는 처음서비스 세팅 된 경우
      if (this.settingData.openSetting.service_stat_code === 5) this.openSettingUseYn = 0
      else this.openSettingUseYn = 1
    },
    showHistory () { // 사용이력 출력
      this.$modal.show(DeliveryModalHistory,
        { title: '택배회수 서비스 사용이력', mall_no: this.searchMallData.mall_no },
        { width: 500, height: 'auto', clickToClose: false, scrollable: true }
      )
    },
    getRefreshData () { // 화면 재조회시 props 가 없는 경우 store 에서 데이터 받아옴
      // console.log(this.$store.getters.getRouteProps.mallSearchInfo)
      // console.log('getRefreshData')

      let mallSearchInfo = this.$store.getters.getRouteProps.mallSearchInfo

      let form = new FormData()
      form.append('mall_id', mallSearchInfo.mall_id)
      form.append('mall_no', mallSearchInfo.mall_no)
      form.append('delivery_company_no', mallSearchInfo.selectedDelivertInfo.delivery_company_no)
      form.append('user_code', 1)
      form.append('user_no', this.userNo)
      form.append('member_id', mallSearchInfo.member_id)
      this.isLoading = true
      http
        // eslint-disable-next-line no-undef
        .post(apiServerUrl + '/serviceSetting/deliveryService/getDeliveryReturnSetting', form)
        .then(response => {
          this.isLoading = false
          // console.log(response)
          if (response.data.resultCode === 0) {
            this.isLoaded = true
            this.settingData = JSON.parse(JSON.stringify(response.data))
            if (!this.settingData.returnPlaceInfo.return_place_tel_sub_no) {
              this.settingData.returnPlaceInfo.return_place_tel_sub_no = ['', '', '']
            }
            if (!this.settingData.returnPlaceInfo.return_place_tel_no) {
              this.settingData.returnPlaceInfo.return_place_tel_no = ['', '', '']
            }
            this.initSettingData = JSON.parse(JSON.stringify(this.settingData))
            this.serviceSettingCommon()
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '시스템 오류.' }, { width: 450, height: 'auto' }
            )
          }
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
    serviceSettingCommon () {
      this.serviceOpenUseYn()
      this.dateTimeParse()
    },
    changeImg (imageId) {
      let imgArr = document.getElementsByClassName('guideTxtImg')
      for (let i = 0; i < imgArr.length; i++) {
        imgArr[i].style.display = 'none'
      }
      document.getElementById(imageId + 'Img').style.display = ''
    },
    setStartDate (value) {
      this.startDate.date = value
    },
    dateTimeParse () { // 택배회수 오픈 설정 사용시작일시 date setting
      // console.log('dateTimeParse')

      if (this.settingData.openSetting.service_start_date === null) {
        // console.log('null 일떄')
        this.startDate.date = ''
        this.startDate.hour = '00'
        this.startDate.time = '00'
      } else {
        // eslint-disable-next-line eqeqeq
        if (this.settingData.openSetting.service_stat_code == 1 || this.settingData.openSetting.service_stat_code == 4) {
          // console.log('오픈 예정 및 일시정지')
          let startDate = this.settingData.openSetting.service_start_date
          this.startDate.date = startDate.split('T')[0]
          let hourTime = startDate.split('T')[1]
          this.startDate.hour = hourTime.substr(0, 2)
          this.startDate.time = hourTime.substr(3, 2)
        } else {
          // console.log('이력 봐야 하는 상태')
          this.startDate.date = ''
          this.startDate.hour = '00'
          this.startDate.time = '00'
        }
      }
    },
    typing: function (evt, type, part, name, idx) {
      let message = evt.target.value.trim()
      message = message.toString()
      let patternNo = /^[0-9]+$/
      let patternNotKo = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/
      // let patternNo = /[^0-9]/g
      if (type === 'no') {
        if (patternNo.test(message) === false) {
          // console.log('숫자만')
          if (message.length !== 0) {
            if (idx !== undefined) {
              this.settingData[part][name][idx] = this.initSettingData[part][name][idx]
            } else {
              this.settingData[part][name] = this.initSettingData[part][name]
            }
          }
          // if (name === 'order_return_day_no') {
          //   this.settingData[part][name] = this.initSettingData[part][name]
          // } if (name === 'order_return_stat_change_day') this.settingData[part][name] = this.initSettingData[part][name]
        }
      } else if (type === 'money') {
        if (patternNo.test(message) === false) {
          if (message.length !== 0) { // 이거 있어야 백스페이스 됨
            if (name === 'delivery_default_price') this.settingData[part][name] = this.initSettingData[part][name]
            if (name === 'order_return_day_no') this.settingData[part][name] = this.initSettingData[part][name]
            if (name === 'order_return_stat_change_day') this.settingData[part][name] = this.initSettingData[part][name]
            if (name === 'delivery_free_order_price') this.settingData[part][name] = this.initSettingData[part][name]
          }
        } else {
          this.settingData[part][name] = parseInt(message)
        }
      } else if (type === 'not_ko') {
        if (patternNotKo.test(message) === true) { // 한글임
          this.settingData[part][name] = this.initSettingData[part][name]
        }
      }
    },
    showBasicModal () {
      let list = []
      list.push('카페24 : 송장입력일 + 1로 부터 지정된 가능일 까지')
      list.push('메샵 : 배송중으로 부터 지정된 가능일 까지')
      this.$modal.show(ModalListAlert,
        { title: '', list: list }, { width: 450, height: 'auto' }
      )
    },
    lenCheck (event, lenlimt, name) {
      // console.log('lenCheck')
      let message = event.target.value.trim()
      if (lenlimt > message.length) {
        if (!this.isModalOpen) { // 중복 모달 제거
          this.isModalOpen = true
          this.$modal.show(ModalAlert,
            { title: '', text: '최소 ' + lenlimt + '자 이상 기재바랍니다.' }, { width: 450, height: 'auto' },
            {
              'closed': (event) => { this.isModalOpen = false }
            }
          )
        }
        if (name === 'delivery_company_auth_no') this.settingData.defaultInfo.delivery_company_auth_no = this.initSettingData.defaultInfo.delivery_company_auth_no
        else if (name === 'delivery_company_mall_no') this.settingData.defaultInfo.delivery_company_mall_no = this.initSettingData.defaultInfo.delivery_company_mall_no
        else if (name === 'delivery_company_branch_name') this.settingData.defaultInfo.delivery_company_branch_name = this.initSettingData.defaultInfo.delivery_company_branch_name
        else if (name === 'mall_delivery_code') this.settingData.defaultInfo.mall_delivery_code = this.initSettingData.defaultInfo.mall_delivery_code
        else if (name === 'return_place_name') this.settingData.returnPlaceInfo.return_place_name = this.initSettingData.returnPlaceInfo.return_place_name
        else if (name === 'delivery_default_price') this.settingData.deliveryPriceSetting.delivery_default_price = this.initSettingData.deliveryPriceSetting.delivery_default_price
        else if (name === 'delivery_free_order_price') this.settingData.deliveryPriceSetting.delivery_free_order_price = this.initSettingData.deliveryPriceSetting.delivery_free_order_price
        else if (name === 'order_return_day_no') this.settingData.deliveryPriceSetting.order_return_day_no = this.initSettingData.deliveryPriceSetting.order_return_day_no
        else if (name === 'order_return_stat_change_day') this.settingData.deliveryPriceSetting.order_return_stat_change_day = this.initSettingData.deliveryPriceSetting.order_return_stat_change_day
      }
    },
    defaultInfoValidation () {
      let isCheck = true

      for (let item in this.valiAlert.defaultInfo) {
        if (!this.settingData.defaultInfo[item] || this.settingData.defaultInfo[item].length < 2) {
          this.$modal.show(ModalAlert,
            { title: '택배회수 정보', text: this.valiAlert.defaultInfo[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return isCheck
    },
    returnPlaceInfoValidation () {
      let isCheck = true

      for (let item in this.valiAlert.returnPlaceInfo) {
        if (item === 'return_place_tel_no') {
          if (this.settingData.returnPlaceInfo[item][0].length < 2 ||
              this.settingData.returnPlaceInfo[item][1].length < 3 ||
              this.settingData.returnPlaceInfo[item][2].length < 3) {
            this.$modal.show(ModalAlert,
              { title: '택배회수 정보', text: this.valiAlert.returnPlaceInfo[item] },
              { width: 450, height: 'auto' }
            )
            return false
          }
        } else if (item === 'return_place_tel_sub_no') {
          if ((this.settingData.returnPlaceInfo[item][0].length + this.settingData.returnPlaceInfo[item][1].length + this.settingData.returnPlaceInfo[item][2].length > 0) &&
              (this.settingData.returnPlaceInfo[item][0].length < 2 || this.settingData.returnPlaceInfo[item][1].length < 3 || this.settingData.returnPlaceInfo[item][2].length < 3)
          ) {
            this.$modal.show(ModalAlert,
              { title: '택배회수 정보', text: this.valiAlert.returnPlaceInfo[item] },
              { width: 450, height: 'auto' }
            )
            return false
          }
        } else if (!this.settingData.returnPlaceInfo[item] || this.settingData.returnPlaceInfo[item].length < 2) {
          this.$modal.show(ModalAlert,
            { title: '택배회수 정보', text: this.valiAlert.returnPlaceInfo[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return isCheck
    },
    deliveryPriceSettingValidation () {
      let isCheck = true

      for (let item in this.valiAlert.deliveryPriceSetting) {
        if (!this.settingData.deliveryPriceSetting[item]) {
          if (typeof this.settingData.deliveryPriceSetting[item] === 'number' && this.settingData.deliveryPriceSetting[item] === 0) {
            continue
          }
          this.$modal.show(ModalAlert,
            { title: '회수택배비 설정', text: this.valiAlert.deliveryPriceSetting[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return isCheck
    },
    guideTextValidation () {
      let isCheck = true
      // console.log('guideTextValidation')
      for (let item in this.valiAlert.guideText) {
        if (!this.settingData.guideText[item]) {
          this.$modal.show(ModalAlert,
            { title: '안내 문구 등록', text: this.valiAlert.guideText[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return isCheck
    },
    openSettingValidation () {
      let isCheck = true
      // console.log('openSettingValidation')
      if (this.settingData.openSetting.service_stat_code === 5) {
        this.$modal.show(ModalAlert,
          { title: '택배회수 오픈 설정', text: '[택배회수 오픈 설정] 서비스 사용 상태를 선택해주세요' },
          { width: 450, height: 'auto' }
        )
        return false
      // eslint-disable-next-line eqeqeq
      } else if (this.settingData.openSetting.service_stat_code == 2) {
        this.$modal.show(ModalAlert,
          { title: '택배회수 오픈 설정', text: '[택배회수 오픈 설정] 사용중으로 변경 불가합니다.' },
          { width: 450, height: 'auto' }
        )
        return false
      }
      // 오픈예정과 일시정지가 아니면 체크 안함
      // eslint-disable-next-line eqeqeq
      if (this.settingData.openSetting.service_stat_code == 1 || this.settingData.openSetting.service_stat_code == 4) {
        for (let item in this.valiAlert.openSetting) {
          if (!this.startDate[item]) {
            this.$modal.show(ModalAlert,
              { title: '택배회수 오픈 설정', text: this.valiAlert.openSetting[item] },
              { width: 450, height: 'auto' }
            )
            return false
          }
        }
      }
      return isCheck
    },
    finalValidation () { // watch 로 변경될떄마다 isChangedData 변경한 데이터로 확인
      let isCheck = true
      for (let item in this.valiAlert.finalInfoSet) {
        if (this.isChangedData[item]) {
          this.$modal.show(ModalAlert,
            { title: '', text: this.valiAlert.finalInfoSet[item] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return isCheck
    },
    openPostcode () {
      this.isOpenedPostcode = true
    },
    setAddress (result) { /** 주소 팝업완료시 주소데이터 set */
      this.settingData.returnPlaceInfo.return_place_addr = result.roadAddress
      this.settingData.returnPlaceInfo.return_place_detail_addr = result.buildingName
      this.settingData.returnPlaceInfo.return_place_zipcode = result.zonecode
    }
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
</style>
