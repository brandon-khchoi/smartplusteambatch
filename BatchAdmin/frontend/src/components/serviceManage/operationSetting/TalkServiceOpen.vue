<template>
    <div class="TalkOpen">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="talkOpenDetailWrap">
                                <div class="titleWrap">
                                    <p class="title">상담톡 서비스 재오픈</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="상담톡 서비스 재오픈">
                                            <caption class="blind">상담톡 서비스 재오픈</caption>
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
                                              <tr>
                                                <th>재오픈 활성화<span class="necessary"></span></th>
                                                <td colspan="2">
                                                  <p class="toggleWrap">
                                                    <label class="switch">
                                                      <input type="checkbox" v-model="changeVisible" :disabled="orgVisible">
                                                      <span class="slider"></span>
                                                    </label>
                                                  </p>
                                                </td>
                                              </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </article>
                    <div class="btnWrap">
                        <ul v-show="corpId !== '' && !orgVisible && changeVisible">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="저장"
                                @click="set()"/>
                            </li>
                        </ul>
                        <p v-if="resultMallData != null && orgVisible && mallId != '9999'">
                          이미 재오픈 활성 상태입니다.
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
  name: 'TalkServiceOpen',
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
      orgVisible: true,
      changeVisible: true
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
              this.orgVisible = true
              this.changeVisible = true
              this.resultMallData = result.data.items.length === 0 ? null : result.data.items
            } else {
              this.resultMallData = null
            }
          })
          .catch(e => {
            console.log('TalkServiceOpen.vue : ' + e)
          })
      }
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/BizMessage/connectionButton/' + this.corpId + '/' + this.mallId)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.orgVisible = result.data.visible
              this.changeVisible = result.data.visible
            } else {
              this.orgVisible = true
              this.changeVisible = true
            }
          })
          .catch(e => {
            this.isLoading = false
            console.log('TalkServiceOpen.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/BizMessage/connectionButton',
            {
              'shopId': this.mallId,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '상담톡 서비스 재오픈 활성화되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = '상담톡 서비스 재오픈 활성화실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '상담톡 서비스 재오픈',
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
            console.log('TalkServiceOpen.vue : ' + e)
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
