<template>
    <div class="Paper">
        <div class="content business paper">
            <h1>용지 구매</h1>
            <div class="formContainer">
                <form action="#" method="post">
                    <div class="searchWrap">
                        <table summary="상품 상세">
                            <caption class="blind">상품 상세</caption>
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
                                <th>상호명<span class="necessary"></span></th>
                                <td colspan="3">
                                  <div>
                                    <input type="text" v-model="corpMallName" disabled/>
                                  </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <article>
                        <div class="articleWrap">
                            <div class="paperDetailWrap">
                                <div class="titleWrap">
                                    <p class="title">상품</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="상품">
                                            <caption class="blind">상품</caption>
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
                                            <tbody v-show="paperData != ''">
                                              <tr>
                                                <th>루나ID</th>
                                                <td style="text-align: right">
                                                  {{paperData.user_id}}
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>상품명</th>
                                                <td>
                                                  <input type="text"
                                                         v-model="paperData.product_name"
                                                         style="width: 100%; text-align: right">
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>가격</th>
                                                <td>
                                                  <input type="number"
                                                         v-model="paperData.price"
                                                         style="width: 100%; text-align: right">
                                                </td>
                                              </tr>
                                              <tr>
                                              <tr>
                                                <th>사용여부<span class="necessary"></span></th>
                                                <td>
                                                  <input type="checkbox" v-model="paperData.use_yn">
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
                        <ul v-show="corpId !== ''">
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
        </div>
        <Spinner v-if="isLoading" />
    </div>
</template>

<script>
import ModalCommonCorpSearch from '../common/modal/ModalCommonCorpSearch'
import ModalAlert from '../common/modal/ModalAlert.vue'
import axios from 'axios'
import { eventBus } from '@/main'

export default {
  name: 'Paper',
  mounted () {
    eventBus.$on('setPaperCorpSearch', item => this.setPaperCorpSearch(item))

    // eslint-disable-next-line no-undef
    let url = apiServerUrl
    this.http = axios.create({baseURL: url})
  },
  data () {
    return {
      isLoading: false,
      corpName: '',
      corpId: '',
      corpMallName: '',
      paperData: []
    }
  },
  methods: {
    corpSearch (name) {
      this.$modal.show(ModalCommonCorpSearch,
        {
          title: '법인/상호명 찾기',
          data: {
            searchTxt: name,
            callBack: 'setPaperCorpSearch'
          }
        }, {
          width: 700,
          height: 'auto'
        }
      )
    },
    setPaperCorpSearch (item) {
      this.corpName = item.member_biz_name
      this.corpId = item.member_id
      this.corpMallName = item.shop_name

      this.get()
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/Paper/getCustomProduct/' + this.corpId)
          .then(result => {
            console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0 && result.data.customProduct !== null) {
              this.paperData = result.data.customProduct
            } else {
              this.paperData = {
                user_id: this.corpId,
                product_name: '',
                price: 0,
                use_yn: false
              }
            }
          })
          .catch(e => {
            console.log('Paper.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        let form = new FormData()
        form.append('user_id', this.paperData.user_id)
        form.append('product_name', this.paperData.product_name)
        form.append('price', this.paperData.price)
        form.append('use_yn', this.paperData.use_yn)

        this.http
          .post('/Paper/setCustomProduct', form)
          .then(result => {
            console.log(result.data)
            this.isLoading = false
            let msg = '상품 등록/수정 되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = '상품 등록/수정 실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '용지구매 상품 등록/수정',
                text: msg
              }, {
                width: 450,
                height: 'auto'
              }
            )
            this.get()
          })
          .catch(e => {
            console.log('Paper.vue : ' + e)
          })
      }
    }
  },
  props: [
    'userMenuAuth'
  ]
}
</script>
