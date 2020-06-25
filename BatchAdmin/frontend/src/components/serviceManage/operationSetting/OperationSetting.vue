<template>
    <div class="Paper">
        <div class="content business paper">
            <h1>운영설정관리</h1>
            <div class="formContainer">
                <form action="#" method="post">
                    <div class="searchWrap">
                        <table summary="설정 상세">
                            <caption class="blind">설정 상세</caption>
                            <colgroup>
                                <col style="width:100px"/>
                                <col style="width:calc(25% - 100px)"/>
                                <col style="width:100px"/>
                                <col style="width:calc(25% - 100px)"/>
                                <col style="width:100px"/>
                                <col style="width:calc(25% - 100px)"/>
                                <col style="width:100px"/>
                                <col/>
                            </colgroup>
                            <tbody>
                            <tr>
                                <th>법인/상호명<span class="necessary"></span></th>
                                <td>
                                  <div class="searchBtnWrap single">
                                      <input type="text" v-model="corpName" @keyup.enter="corpSearch(corpName)"/>
                                      <p class="btn_search" @click="corpSearch(corpName)">찾기</p>
                                  </div>
                                </td>
                                <th>루나ID<span class="necessary"></span></th>
                                <td>
                                  <div>
                                    <input type="text" v-model="corpId" disabled/>
                                  </div>
                                </td>
                                <th>설정<span class="necessary"></span></th>
                                <td>
                                  <select v-model="setModeValue" :disabled="corpId === ''">
                                    <option disabled value="">선택하세요</option>
                                    <option value="4244">상담톡 메시지 변경</option><!--ChatBotMessages-->
                                    <option value="4245">상담톡 고도몰 API키 등록</option><!--GodoMallApiKey-->
                                    <option value="4246">상담톡 통합상담 버튼 노출</option><!--ChattingAllState-->
                                    <option value="4301">담당자 전화번호 정보 변경</option><!--TelAndEmailInfo-->
                                    <option disabled value="4303">카카오아이 월 사용료 과금 수정</option>
                                    <option value="4305">알림톡 서비스 충전(+)</option><!--TalkServiceCharge-->
                                    <option value="4305_2">알림톡 서비스 차감(-)</option><!--TalkServiceChargeDeduction-->
                                    <option disabled value="4307">카카오i 요금제 구간 변경</option>
                                    <option value="4308">CTI사용료 차감(-)</option><!--CtiFeeDeduct-->
                                    <option disabled value="4309">세팅 설정 불가 확인</option>
                                    <option value="4310">상담톡 포인트 충전(+)</option><!--ConsultingTalkPoint-->
                                    <option value="4310_2">상담톡 포인트 차감(-)</option><!--ConsultingTalkPointDeduction-->

                                    <option value="4568">상담톡 서비스 재오픈</option><!--TalkServiceOpen-->
                                    <option value="4810">상담톡 인증 초기화 요청</option><!--TalkCustomerAuth-->
                                    <option value="4811">상담건 종료처리</option><!--ConsultingEndAll-->
                                    <option value="4567">제네릭버튼 상담원으로 전환하기 타입 변경</option><!--KakaoiGenericMenu-->
                                    <option value="5860">중복로그인 설정</option><!--MemberDupLoginConfig-->

                                    <option value="6647">접근 IP주소 설정</option>AccessIPConfig
                                    <option value="6640">스팸 키워드 설정</option><!--SpamKeywordConfig-->

                                  </select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <article>
                        <div class="articleWrap">
                          <chat-bot-messages
                            v-if="setModeValue === '4244'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></chat-bot-messages>
                          <godo-mall-api-key
                            v-if="setModeValue === '4245'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></godo-mall-api-key>
                          <chatting-all-state
                            v-if="setModeValue === '4246'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></chatting-all-state>
                          <tel-and-email-info
                            v-if="setModeValue === '4301'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></tel-and-email-info>
                          <talk-service-charge
                            v-if="setModeValue === '4305'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></talk-service-charge>
                          <talk-service-charge-deduction
                            v-if="setModeValue === '4305_2'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></talk-service-charge-deduction>
                          <cti-fee-deduct
                            v-if="setModeValue === '4308'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></cti-fee-deduct>
                          <consulting-talk-point
                            v-if="setModeValue === '4310'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></consulting-talk-point>
                          <consulting-talk-point-deduction
                            v-if="setModeValue === '4310_2'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></consulting-talk-point-deduction>
                          <talk-service-open
                            v-if="setModeValue === '4568'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></talk-service-open>
                          <talk-customer-auth
                            v-if="setModeValue === '4810'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></talk-customer-auth>
                          <consulting-end-all
                            v-if="setModeValue === '4811'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></consulting-end-all>
                          <kakaoi-generic-menu
                            v-if="setModeValue === '4567'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></kakaoi-generic-menu>
                          <member-dup-login-config
                            v-if="setModeValue === '5860'" :corpNo=corpNo :http=http :userMenuAuth=userMenuAuth ></member-dup-login-config>

                          <access-ip-config
                            v-if="setModeValue === '6647'" :corpId=corpId :corpNo=corpNo :http=http :userMenuAuth=userMenuAuth ></access-ip-config>
                          <spam-keyword-config
                            v-if="setModeValue === '6640'" :corpId=corpId :http=http :userMenuAuth=userMenuAuth ></spam-keyword-config>
                        </div>
                    </article>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import ModalCommonCorpSearch from '@/components/common/modal/ModalCommonCorpSearch'
import axios from 'axios'
import { eventBus } from '@/main'

import ChatBotMessages from './ChatBotMessages'
import ChattingAllState from './ChattingAllState'
import GodoMallApiKey from './GodoMallApiKey'
import TelAndEmailInfo from './TelAndEmailInfo'
import TalkServiceCharge from './TalkServiceCharge'
import TalkServiceChargeDeduction from './TalkServiceChargeDeduction'
import CtiFeeDeduct from './CtiFeeDeduct'
import ConsultingTalkPoint from './ConsultingTalkPoint'
import ConsultingTalkPointDeduction from './ConsultingTalkPointDeduction'
import TalkServiceOpen from './TalkServiceOpen'
import TalkCustomerAuth from './TalkCustomerAuth'
import ConsultingEndAll from './ConsultingEndAll'
import KakaoiGenericMenu from './KakaoiGenericMenu'
import MemberDupLoginConfig from './MemberDupLoginConfig'
import AccessIPConfig from './AccessIPConfig'
import SpamKeywordConfig from './SpamKeywordConfig'

export default {
  name: 'OperationSetting',
  created () {
    eventBus.$on('setOperationSettingCorpSearch', item => this.setOperationSettingCorpSearch(item))

    // eslint-disable-next-line no-undef
    let url = apiServerUrl
    this.http = axios.create({baseURL: url})
  },
  mounted () {
  },
  beforeDestroy () {
    eventBus.$off('setOperationSettingCorpSearch')
  },
  data () {
    return {
      setModeValue: '', // pms등록 ID와 동일값
      corpName: '',
      corpId: '',
      corpNo: ''
    }
  },
  methods: {
    corpSearch (name) {
      this.$modal.show(ModalCommonCorpSearch,
        {
          title: '법인/상호명 찾기',
          data: {
            searchTxt: name,
            callBack: 'setOperationSettingCorpSearch'
          }
        }, {
          width: 700,
          height: 'auto'
        }
      )
    },
    setOperationSettingCorpSearch (item) {
      this.corpName = item.member_biz_name
      this.corpId = item.member_id
      this.corpNo = item.member_no
      this.setModeValue = ''
    }
  },
  components: {
    'chat-bot-messages': ChatBotMessages,
    'chatting-all-state': ChattingAllState,
    'godo-mall-api-key': GodoMallApiKey,
    'tel-and-email-info': TelAndEmailInfo,
    'talk-service-charge': TalkServiceCharge,
    'talk-service-charge-deduction': TalkServiceChargeDeduction,
    'cti-fee-deduct': CtiFeeDeduct,
    'consulting-talk-point': ConsultingTalkPoint,
    'consulting-talk-point-deduction': ConsultingTalkPointDeduction,
    'talk-service-open': TalkServiceOpen,
    'talk-customer-auth': TalkCustomerAuth,
    'consulting-end-all': ConsultingEndAll,
    'kakaoi-generic-menu': KakaoiGenericMenu,
    'member-dup-login-config': MemberDupLoginConfig,
    'access-ip-config': AccessIPConfig,
    'spam-keyword-config': SpamKeywordConfig
  },
  props: [
    'userMenuAuth'
  ]
}
</script>
