<template>
    <div class="Paper">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="paperDetailWrap">
                                <div class="titleWrap">
                                  <p class="title">스팸 키워드 설정</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="스팸 키워드 설정">
                                            <caption class="blind">스팸 키워드 설정</caption>
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
                                                  <th>쇼핑몰 검색</th>
                                                  <td colspan="2">
                                                    <div class="searchBtnWrap single">
                                                      <input type="text"
                                                             v-model="mallName"
                                                             @keyup.enter="mallSearch(mallName)"
                                                             style="width:70%;">
                                                      <p class="btn_search" @click="mallSearch(mallName)">찾기</p>
                                                      <input hidden>
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
                                                <tr>
                                                  <th>스팸 키워드</th>
                                                  <td colspan="7">
                                                    <h5>* 스팸 키워드는 ' , ' 를 기준으로 구분 및 적용됩니다.</h5>
                                                    <textarea :disabled="isModify===false"
                                                           v-model="resultData"
                                                           style="width: 100%; height: 200px;"/>
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
                        <ul v-show="mallId !== '9999' && isModify===false && resultMallData">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="수정"
                                @click="funcModifyStat()"/>
                            </li>
                        </ul>
                        <ul v-show="mallId !== '9999' && isModify===true && resultMallData">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_cancel btn-lg"
                                type="button"
                                value="취소"
                                @click="funcModifyCancel()"/>
                            </li>
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="저장"
                                @click="set()"/>
                            </li>
                            <li style="float:right;">
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="기본값 초기화"
                                @click="funcResetDefault()"/>
                            </li>
                        </ul>
                      <div>
                        <p style="float:left;"><strong><i class="far fa-exclamation-circle"></i> 기본 스팸 키워드 목록</strong></p>
                        <textarea disabled v-model="defaultKeyword" style="margin-top: 20px; float:left; width: 100%; height: 200px;"/>
                      </div>
                    </div>
                </form>
            </div>
        <Spinner v-if="isLoading" />
    </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert.vue'

export default {
  name: 'SpamKeywordConfig',
  created () {
  },
  mounted () {
    // this.get()
  },
  data () {
    return {
      isLoading: false,
      isModify: false,
      mallName: '',
      mallId: '9999', // 임의 쓰레기값
      resultMallData: null,
      resultData: '',
      resultDataOrg: '',
      defaultKeyword: ''
    }
  },
  methods: {
    mallSearch (txt) {
      if (this.userMenuAuth.retrieve_list) {
        this.mallId = '9999'
        this.http
          .get('/DataCorrection/BizMessage/shop/' + this.corpId + '/' + this.mallName)
          .then(result => {
            if (result.data.resultCode === 0) {
              this.resultData = ''
              this.resultMallData = result.data.items.length === 0 ? null : result.data.items
            } else {
              this.resultMallData = null
            }
          })
          .catch(e => {
            console.log('SpamKeywordConfig.vue : ' + e)
          })
      }
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.isModify = false
        this.http
          .get('/DataCorrection/AlimTalk/taboo/' + this.corpId + '/' + this.mallId)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultData = result.data.taboo
              this.resultDataOrg = result.data.taboo
              this.defaultKeyword = result.data.defaultTaboo
            } else {
              this.resultData = ''
              this.resultDataOrg = ''
              this.defaultKeyword = ''
              this.isModify = false
            }
          })
          .catch(e => {
            console.log('SpamKeywordConfig.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/AlimTalk/taboo',
            {
              'shopId': this.mallId,
              'taboo': this.resultData,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '스팸 키워드 설정처리 되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = '스팸 키워드 설정처리 실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '스팸 키워드 설정',
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
            console.log('SpamKeywordConfig.vue : ' + e)
          })
      }
    },
    /**
     * 수정버튼클릭상태 값 반전처리
     */
    funcModifyStat () {
      this.isModify = !this.isModify
    },
    /**
     * 수정취소
     */
    funcModifyCancel () {
      this.resultData = this.resultDataOrg
      this.funcModifyStat()
    },
    /**
     * 스팸키워드 초기화
     */
    funcResetDefault () {
      this.resultData = this.defaultKeyword
    }
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
