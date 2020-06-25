<template>
    <div class="SaleCreate">
        <div class="content business sale">
            <h1>영업활동 등록</h1>
            <div class="formContainer">
                <div class="searchWrap">
                    <table summary="영업활동 등록">
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
                            <th>법인/상호명<span class="necessary">*</span></th>
                            <td>
                                <div class="searchBtnWrap single">
                                    <input type="text" v-model="corpName" @keyup.enter="corpSearch(corpName)"/>
                                    <p class="btn_search" @click="corpSearch(corpName)">찾기</p>
                                </div>
                            </td>
                            <th>유입경로<span class="necessary">*</span></th>
                            <td>
                                <select v-model="memberInflowPathCode">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in memberInflowPathCodeData">
                                        <option :value="idx" :key="val">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                            <th>영업일<span class="necessary">*</span></th>
                            <td>
                                <p class="calendarWrapper single">
                                    <datepicker
                                        class="datepicker"
                                        format="yyyy-MM-dd"
                                        :language="languages['ko']"
                                        v-model="salesDate"
                                    />
                                </p>
                            </td>
                            <th>영업담당<span class="necessary">*</span></th>
                            <td>
                                <select v-model="managerSalesLunaNo">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in managerSalesLunaNoData">
                                        <option :value="idx" :key="val">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <article>
                    <div class="articleWrap">
                        <div class="saleDetailWrap">
                            <div class="titleWrap">
                                <p class="title">영업내용</p>
                                <div class="btnWrap">
                                    <ul>
                                        <li><p class="btn_add" @click="dataPush()"><i class="fas fa-plus"></i>영업내용 추가</p></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="detailContent">
                                <table summary="영업내용 추가">
                                    <caption class="blind">영업내용 추가</caption>
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
                                    <template v-for="(item, idx) in newSalesData">
                                        <tr :key="idx">
                                            <td colspan="4" class="textLeft">
                                                <p>영업 {{idx+1}}</p>
                                            </td>
                                            <td colspan="4" class="textRight">
                                                <p class="btn_delete" @click="dataPull(idx)"><i class="fas fa-times"></i>삭제</p>
                                            </td>
                                        </tr>
                                        <tr :key="idx.empty_salesCode">
                                            <th>영업항목<span class="necessary">*</span></th>
                                            <td>
                                                <select v-model="item.salesCode">
                                                    <option value="">전체</option>
                                                    <template v-for="(val, idx) in salesCodeData">
                                                        <option :value="idx" :key="val">{{val}}</option>
                                                    </template>
                                                </select>
                                            </td>
                                            <th>영업상태<span class="necessary">*</span></th>
                                            <td>
                                                <select v-model="item.salesStatCode">
                                                    <option value="">전체</option>
                                                    <template v-for="(val, idx) in salesStatCodeData">
                                                        <option :value="idx" :key="val">{{val}}</option>
                                                    </template>
                                                </select>
                                            </td>
                                            <th>영업형태<span class="necessary">*</span></th>
                                            <td>
                                                <select v-model="item.salesTypeCode">
                                                    <option value="">전체</option>
                                                    <template v-for="(val, idx) in salesTypeCodeData">
                                                        <option :value="idx" :key="val">{{val}}</option>
                                                    </template>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr :key="idx.empty_salesText">
                                            <td colspan="8">
                                                <textarea rows="7"
                                                    :placeholder="'담당자 명 : \n직위 : \n연락처 : \n내용 :'"
                                                    v-model="item.salesText">
                                                </textarea>
                                            </td>
                                        </tr>
                                    </template>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </article>
                <div class="btnWrap btnEnd" style="text-align:center;">
                    <ul>
                        <li><input class="btn_cancel btn-lg" type="button" value="취소"
                                   @click="goList"/></li>
                        <li>
                            <input class="btn_save btn-lg" type="button" value="저장" @click="create()"/>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/dist/locale'
import ModalAlert from '../common/modal/ModalAlert.vue'
import ModalSalesCorpSearch from './ModalSalesCorpSearch.vue'
import { eventBus } from '../../main'
import http from '../../http-commons'

export default {
  mounted () {
    this.salesDate = new Date()
    eventBus.$on('setCorpSearch', item => this.setCorpSearch(item))

    // 영업담당자 세팅
    this.setSalesLunaUser()
  },
  data () {
    return {
      languages: lang,
      salesDate: '',
      corpName: '',
      searchCorpName: '', // 법입/상호명 조회 후 corpName 수정여부를 판단하기위한 org 값
      memberNo: '',
      mallNo: '',
      memberInflowPathCode: '',
      managerSalesLunaNo: '',
      newSalesData: [
        {
          salesCode: '',
          salesStatCode: '',
          salesTypeCode: '',
          salesText: ''
        }
      ],
      valiAlert: {
        salesCode: '영업항목을 선택해주세요.',
        salesStatCode: '영업상태를 선택해주세요.',
        salesTypeCode: '영업형태를 선택해주세요.',
        salesText: '영업내용을 입력해주세요.'
      }
    }
  },
  methods: {
    goList () {
      this.$router.go(-1)
    },
    setSalesLunaUser () {
      // 로그인 사용자가 영업담당자인경우 화면 기본세팅
      let userNo = this.$store.getters.getUser.luna_no
      let salesUser = this.managerSalesLunaNoData[userNo]
      if (salesUser) {
        this.managerSalesLunaNo = salesUser
      }
    },
    dataPush () {
      let len = this.newSalesData.length
      if (len < 3) {
        this.newSalesData.push({
          salesCode: '',
          salesStatCode: '',
          salesTypeCode: '',
          salesText: ''
        })
      } else {
        this.$modal.show(ModalAlert,
          {
            title: '영업등록',
            text: len + '개 까지만 등록 가능합니다.'
          }, {
            width: 450,
            height: 167
          }
        )
      }
    },
    dataPull (idx) {
      if (this.newSalesData.length === 1) {
        return false
      }
      this.newSalesData.splice(idx, 1)
    },
    corpSearch (name) {
      this.$modal.show(ModalSalesCorpSearch,
        {
          title: '법인/상호명 찾기',
          text: {
            searchTxt: name
          }
        }, {
          width: 700,
          height: 'auto'
        }
      )
    },
    setCorpSearch (item) {
      this.memberNo = item.member_no
      this.mallNo = item.mall_no
      this.memberInflowPathCode = item.member_inflow_path_code == null ? '' : item.member_inflow_path_code
      this.corpName = item.mall_name
      this.searchCorpName = item.mall_name
    },
    create () {
      if (!this.validation()) return false

      http
        .post('/sales/salescreate', {
          params: {
            member_no: this.memberNo,
            mall_no: this.mallNo,
            member_inflow_path_code: this.memberInflowPathCode,
            manager_sales_luna_no: this.managerSalesLunaNo,
            // eslint-disable-next-line no-undef
            sales_date: dateFormatToString(this.salesDate),
            sales_data: this.newSalesData,
            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(result => {
          var msg = '영업정보가 등록되었습니다.'
          if (result.data !== '0000') {
            msg = '영업정보 등록에 실패했습니다.'
          }
          this.$modal.show(ModalAlert,
            {
              title: '영업등록 생성',
              text: msg
            }, {
              width: 450,
              height: 'auto'
            }
          )

          if (result.data === '0000') {
            this.$router.push({name: 'saleslist'})
          }
        })
        .catch(e => {
          console.log('SalesCreate.vue : ' + e)
        })
    },
    validation () {
      let msg = null
      if (this.memberNo === '' || this.mallNo === '' || this.corpName !== this.searchCorpName) msg = '법인/상호명을 조회해주세요.'
      if (this.managerSalesLunaNo === '') msg = '영업담당자를 선택해주세요.'
      if (this.memberInflowPathCode === '') msg = '유입경로를 선택해주세요'
      if (msg != null) {
        this.$modal.show(ModalAlert,
          {
            title: '영업 등록',
            text: msg
          }, {
            width: 450,
            height: 'auto'
          }
        )
        return false
      }

      for (let i in this.newSalesData) {
        for (let j in this.valiAlert) {
          if (!this.newSalesData[i][j]) {
            this.$modal.show(ModalAlert,
              {
                title: '영업 등록',
                text: Number(i + 1) + ' 번째' + this.valiAlert[j]
              }, {
                width: 450,
                height: 'auto'
              }
            )
            return false
          }
        }
      }
      return true
    }
  },
  components: {
    Datepicker
  },
  props: [
    'userMenuAuth',
    'memberInflowPathCodeData',
    'managerSalesLunaNoData',
    'salesCodeData',
    'salesStatCodeData',
    'salesTypeCodeData'
  ]
}
</script>
