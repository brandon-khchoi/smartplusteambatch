<template>
    <div class="KakaoiGenericMenu">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="kakaoiGenericMenuDetailWrap">
                                <div class="titleWrap">
                                    <p class="title">제네릭버튼 상담원으로 전환하기 타입 변경</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="제네릭버튼 상담원으로 전환하기 타입 변경">
                                            <caption class="blind">제네릭버튼 상담원으로 전환하기 타입 변경</caption>
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
                                                <th>쇼핑몰 검색</th>
                                                <td colspan="2">
                                                  <div class="searchBtnWrap single">
                                                    <input type="text"
                                                            v-model="mallName"
                                                            @keyup.enter="mallSearch(mallName)"
                                                            style="width:70%;">
                                                    <p class="btn_search" @click="mallSearch(mallName)">찾기</p>
                                                    <input type="text" hidden>
                                                  </div>
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>쇼핑몰 선택</th>
                                                <td colspan="2">
                                                  <select :disabled="resultMallData===null"
                                                          @change="get()"
                                                          style="width: 100%;"
                                                          v-model="mallId">
                                                    <option value="9999">선택하세요</option>
                                                    <template v-for="item in resultMallData">
                                                      <option
                                                        :key="item.id"
                                                        :value="item.id">
                                                        {{item.name}}</option>
                                                    </template>
                                                  </select>
                                                </td>
                                              </tr>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div v-if="resultData != null" class="tableWrap">
                                      <table summary="고객 조회결과">
                                          <caption class="blind">고객 조회결과</caption>
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
                                            <template>
                                              <tr>
                                                <th>제네릭 <p>버튼 타입</p><span class="necessary"></span></th>
                                                <td colspan="2">
                                                  <select v-model="modGenericType"
                                                          style="width: 100%;">
                                                    <option v-for="item in genericDataList"
                                                            :disabled="item.code === resultData.kakaoiGenericMenuCode"
                                                            :key="item.code"
                                                            :value="item.code">
                                                      {{item.name}}
                                                    </option>
                                                  </select>
                                                </td>
                                              </tr>
                                            </template>
                                          </tbody>
                                      </table>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </article>
                    <div class="btnWrap">
                        <ul v-show="corpId !== '' && (resultData != null && resultData.kakaoiGenericMenuCode !== modGenericType)">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="저장"
                                @click="set()"/>
                            </li>
                        </ul>
                    </div>
                </form>
            </div>
        <Spinner v-if="isLoading" />
    </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert.vue'

export default {
  name: 'KakaoiGenericMenu',
  created () {
  },
  mounted () {
    // this.get()
  },
  data () {
    return {
      isLoading: false,
      mallName: '',
      mallId: '9999', // 임의 쓰레기값
      resultMallData: null,
      modGenericType: '',
      genericDataList: [
        {code: 0, name: '강제 챗봇 전환'},
        {code: 1, name: '챗봇 전환 블록 실행'},
        {code: 2, name: '상담톡 챗봇 실행'},
        {code: 3, name: '상담원 연결'}
      ],
      resultData: null
    }
  },
  methods: {
    mallSearch (txt) {
      if (this.userMenuAuth.retrieve_list) {
        this.mallId = '9999'
        this.dataReset()
        this.http
          .get('/DataCorrection/BizMessage/shop/' + this.corpId + '/' + this.mallName)
          .then(result => {
            if (result.data.resultCode === 0) {
              this.resultMallData = result.data.items.length === 0 ? null : result.data.items
            }
          })
          .catch(e => {
            console.log('KakaoiGenericMenu.vue : ' + e)
          })
      }
    },
    get () {
      if (this.userMenuAuth.retrieve_list && this.mallId !== '9999') {
        this.isLoading = true
        this.http
          .get('/DataCorrection/BizMessage/kakaoiGenericMenu/' + this.corpId + '/' + this.mallId)
          .then(result => {
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultData = result.data
              this.modGenericType = result.data.kakaoiGenericMenuCode
            }
          })
          .catch(e => {
            this.isLoading = false
            console.log('KakaoiGenericMenu.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/BizMessage/kakaoiGenericMenu',
            {
              'shopId': this.mallId,
              'kakaoiGenericMenu': this.modGenericType,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            this.isLoading = false
            let msg = '제네릭버튼 상담원으로 전환하기 타입 변경되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = result.data.message
            }
            this.$modal.show(ModalAlert,
              {
                title: '제네릭버튼 상담원으로 전환하기 타입 변경',
                text: msg
              }, {
                width: 450,
                height: 'auto'
              }
            )
            this.get()
          })
          .catch(e => {
            this.isLoading = false
            console.log('KakaoiGenericMenu.vue : ' + e)
          })
      }
    },
    dataReset () {
      this.resultMallData = null
      this.resultData = null
    }
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
