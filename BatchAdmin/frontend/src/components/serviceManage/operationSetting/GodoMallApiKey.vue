<template>
    <div class="Paper">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="paperDetailWrap">
                                <div class="titleWrap">
                                  <p class="title">상담톡 고도몰 API키 등록</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="상담톡 고도몰 API키 등록">
                                            <caption class="blind">상담톡 고도몰 API키 등록</caption>
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
                                                  <th>쇼핑몰 타입</th>
                                                  <td colspan="2">
                                                    {{resultData.shopType}}
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <th>고도몰 API Key</th>
                                                  <td colspan="2">
                                                    <input type="text"
                                                           maxlength="100"
                                                           :disabled="mallId === '9999'"
                                                           v-model="resultData.godomallAPIKey"
                                                           style="width: 100%;"/>
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
                        <ul v-show="corpId !== '' && resultMallData">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="저장"
                                @click="set()"/>
                            </li>
                        </ul>
                        <p v-if="!resultMallData">
                          등록 정보가 없어 수정이 불가합니다.
                        </p>
                    </div>
                </form>
            </div>
        <Spinner v-if="isLoading" />
    </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert.vue'

export default {
  name: 'GodoMallApiKey',
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
      resultData: {}
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
              this.resultData = {}
              this.resultMallData = result.data.items.length === 0 ? null : result.data.items
            } else {
              this.resultMallData = null
            }
          })
          .catch(e => {
            console.log('GodoMallApiKey.vue : ' + e)
          })
      }
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/BizMessage/godomallAPIKey/' + this.corpId + '/' + this.mallId)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultData = result.data
            } else {
              this.resultData = {}
            }
          })
          .catch(e => {
            console.log('GodoMallApiKey.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/BizMessage/godomallAPIKey',
            {
              'shopId': this.mallId,
              'godomallAPIKey': this.resultData.godomallAPIKey,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '상담톡 고도몰 API키 등록처리 되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = '상담톡 고도몰 API키 등록처리 실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '상담톡 고도몰 API키 등록',
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
            console.log('GodoMallApiKey.vue : ' + e)
          })
      }
    }
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
