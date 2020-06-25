<template>
    <div name="modal-corp-search" class="ModalCorpSearch">
        <div class="vue-modal-box">
            <div class="dialog-content">
                <div class="dialog-c-title">{{ title }}</div>
                <div class="dialog-c-text">
                    <div class="popupContentWrap">
                        <div class="popupContent">
                            <div class="searchBtnWrap">
                                <input type="text" minlength="2" v-model="name" @keyup.enter="get()">
                                <p class="btn_search" @click="get()">검색</p>
                            </div>
                            <article>
                                <div class="articleWrap">
                                    <div class="tableWrap tableList scrollWrap">
                                        <table summary="법인/상호명 찾기">
                                            <caption class="blind">법인/상호명 찾기</caption>
                                            <colgroup>
                                                <col width="150px"/>
                                                <col width="200px"/>
                                                <col width="*"/>
                                                <col width="70px"/>
                                            </colgroup>
                                            <thead>
                                            <th>ID</th>
                                            <th>법인명</th>
                                            <th>상호명</th>
                                            <th>선택</th>
                                            </thead>
                                            <tbody>
                                              <template v-if="list.length != 0">
                                                <tr v-for="item in list" :key="item.member_id">
                                                    <td class="text-center">{{item.member_id}}</td>
                                                    <td class="text-center">{{item.member_biz_name}}</td>
                                                    <td class="text-center">{{item.shop_name}}</td>
                                                    <td class="text-center"><input class="btn_min" type="button" value="선택" @click="set(item)"/></td>
                                                </tr>
                                              </template>
                                              <tr v-else>
                                                <td colspan="4" class="emptyTable">
                                                  해당하는 법인/상호명이 없습니다.
                                                </td>
                                              </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </article>
                        </div>
                    </div>
                </div>
            </div>
            <div class="dialog-button-wrapper">
                <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
            </div>
        </div>
    </div>
</template>

<script>
import { eventBus } from '../../main'
import ModalAlert from '../common/modal/ModalAlert.vue'
import http from '../../http-commons'

export default {
  mounted () {
    this.get()
  },
  data () {
    return {
      name: this.text.searchTxt,
      list: []
    }
  },
  methods: {
    get () {
      if (!this.chkTxtLen()) return false

      http
        .get('/sales/salescorpsearch',
          {
            params: {
              search_txt: this.name
            }

          })
        .then(result => {
          this.list = result.data
        })
        .catch(e => {
          console.log('ModalSalesCorpSearch.vue : ' + e)
        })
    },
    chkTxtLen () {
      let len = this.name.length
      if (len > 1) {
        return true
      } else {
        this.$modal.show(ModalAlert,
          {
            title: this.title,
            text: '검색어를 2글자 이상 입력하세요.'
          }, {
            width: 450,
            height: 'auto'
          }
        )
        return false
      }
    },
    set (item) {
      eventBus.$emit('setCorpSearch', item)
      this.$emit('close')
    }
  },
  props: ['text', 'title']
}
</script>
