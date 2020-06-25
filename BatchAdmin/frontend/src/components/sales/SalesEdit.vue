<template>
    <div class="SaleList">
        <div class="content business sale">
            <h1>영업 상세</h1>
            <div class="formContainer">
                <form action="#" method="post">
                    <div class="searchWrap">
                        <table summary="영업활동 상세">
                            <caption class="blind">영업 상세</caption>
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
                            <tbody v-if="salesData != ''">
                            <tr>
                                <th>법인/상호명<span class="necessary"></span></th>
                                <td>
                                    {{salesData[0].mall_name}}
                                </td>
                                <th>유입경로<span class="necessary"></span></th>
                                <td>
                                    {{memberInflowPathCodeData[salesData[0].member_inflow_path_code]}}
                                </td>
                                <th>가입일자<span class="necessary"></span></th>
                                <td>
                                    {{salesData[0].join_date}}
                                </td>
                                <th>영업담당자<span class="necessary"></span></th>
                                <td>
                                    {{managerSalesLunaNoData[salesData[0].manager_sales_luna_no]}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="searchWrap">
                        <table>
                            <caption class="blind">영업 상세</caption>
                            <colgroup>
                                <col style="width:calc(25% - 100px)"/>
                                <col style="width:100px"/>
                                <col style="width:calc(25% - 100px)"/>
                                <col style="width:100px"/>
                                <col style="width:calc(25% - 100px)"/>
                                <col style="width:100px"/>
                                <col style="width:calc(25% - 100px)"/>
                                <col style="width:100px"/>
                                <col style="width:calc(25% - 100px)"/>
                                <col/>
                            </colgroup>
                            <tbody>
                            <tr>
                                <th style="font-weight: bold">필터</th>
                                <th>영업상태<span class="necessary"></span></th>
                                <td>
                                    <select v-model="salesStatFt" @change="get()">
                                        <option value="">선택하세요</option>
                                        <template v-for="(val, idx) in salesStatCodeData">
                                            <option :value="idx" :key="idx">{{val}}</option>
                                        </template>
                                    </select>
                                </td>
                                <th>영업등록일<span class="necessary"></span></th>
                                <td>
                                    <datepicker
                                        class="datepicker"
                                        :language="languages['ko']"
                                        format="yyyy-MM-dd"
                                        v-model="salesDateFt"
                                    />
                                </td>
                                <th>등록자<span class="necessary"></span></th>
                                <td>
                                    <select v-model="salesRegUserFt" @change="get()">
                                        <option value="">선택하세요</option>
                                        <template v-for="(val, idx) in lunaAccount">
                                            <option :value="idx" :key="idx">{{val}}</option>
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
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="영업내용 수정">
                                            <caption class="blind">영업내용 수정</caption>
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
                                            <template v-for="(item, idx) in salesData">
                                                <tr :key="item.edit">
                                                    <td colspan="10" class="textLeft">
                                                        <p>영업 {{idx+1}}</p>
                                                        <span style="float: right;">
                                                           <p class="btn_min" style="width: 60px;"
                                                              @click="editOpen(item)"
                                                              v-if="!item.edit">수정</p>
                                                           <p class="btn_min" style="width: 60px;"
                                                              @click="editSet(item)"
                                                              v-else>저장</p>

                                                           <p class="btn_min" style="width: 60px;"
                                                              @click="editDel(item)"
                                                              v-if="!item.edit">삭제</p>
                                                           <p class="btn_min" style="width: 60px;"
                                                              @click="editCancel(item)"
                                                              v-else>취소</p>
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr :key="item.sales_code">
                                                    <th>영업항목<span class="necessary">*</span></th>
                                                    <td>
                                                        <select v-model="item.sales_code" v-if="item.edit == true">
                                                            <option value="">전체</option>
                                                            <template v-for="(val, idx) in salesCodeData">
                                                                <option :value="idx" :key="idx">{{val}}</option>
                                                            </template>
                                                        </select>
                                                        <p v-else>{{salesCodeData[item.sales_code]}}</p>
                                                    </td>
                                                    <th>영업상태<span class="necessary">*</span></th>
                                                    <td>
                                                        <select v-model="item.sales_stat_code" v-if="item.edit == true">
                                                            <option value="">전체</option>
                                                            <template v-for="(val, idx) in salesStatCodeData">
                                                                <option :value="idx" :key="idx">{{val}}</option>
                                                            </template>
                                                        </select>
                                                        <p v-else>{{salesStatCodeData[item.sales_stat_code]}}</p>
                                                    </td>
                                                    <th>영업일<span class="necessary">*</span></th>
                                                    <td>
                                                        <datepicker
                                                            class="datepicker"
                                                            format="yyyy-MM-dd"
                                                            :language="languages['ko']"
                                                            v-model="item.sales_date"
                                                            v-if="item.edit == true"
                                                        />
                                                        <p v-else>{{item.sales_date}}</p>
                                                    </td>
                                                    <th>등록일<span class="necessary"></span></th>
                                                    <td>
                                                        {{item.sales_reg_date}}
                                                    </td>
                                                    <th>등록자<span class="necessary"></span></th>
                                                    <td>
                                                        {{lunaAccount[item.create_user_no]}}
                                                    </td>
                                                </tr>
                                                <tr :key="idx">
                                                    <td colspan="10">
                                                        <textarea v-if="item.edit == true"
                                                                  rows="7"
                                                                  v-model="item.sales_text"
                                                                  v-text="item.sales_text"
                                                                  ></textarea>
                                                        <p rows="7" v-else>{{item.sales_text}}</p>
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
                        <ul>
                            <li @click="goList"><p class="btn_cancel btn-lg">목록</p></li>
                        </ul>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
// 영업상세
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/dist/locale'
import http from '../../http-commons'
import ModalAlert from '../common/modal/ModalAlert.vue'
import ModalConfirm from '../common/modal/ModalConfirm.vue'
import { eventBus } from '../../main'

export default {
  mounted () {
    eventBus.$on('refreshSalesDetail', () => this.get())
    this.get()
  },
  watch: {
    salesDateFt () {
      this.get()
    }
  },
  data () {
    return {
      loading: false,
      languages: lang,
      salesCodeFt: '',
      salesStatFt: '',
      salesDateFt: '',
      salesRegUserFt: '',
      memberInflowPathCode: '',
      managerSalesLunaNo: '',
      salesData: []
    }
  },
  methods: {
    goList () {
      this.$router.go(-1)
    },
    get () {
      http
        .get('/sales/salesedit', {
          params: {
            mall_no: this.$route.params.salesDtailInfo.mall_no,
            sales_code: this.$route.params.salesDtailInfo.sales_code,
            sales_stat_ft: this.salesStatFt,
            // eslint-disable-next-line no-undef
            sales_date_ft: this.salesDateFt === '' ? '' : dateFormatToString(this.salesDateFt),
            sales_reg_user_ft: this.salesRegUserFt
          }
        })
        .then(result => {
          this.salesData = result.data
        })
        .catch(e => {
          console.log('SalesEdit.vue : ' + e)
        })
    },
    editOpen (item) {
      item.edit = true
      item.origin_sales_code = item.sales_code
      item.origin_sales_stat_code = item.sales_stat_code
      item.origin_sales_date = item.sales_date
      item.origin_sales_text = item.sales_text
      this.$forceUpdate()
    },
    editCancel (item) {
      item.edit = false
      item.sales_code = item.origin_sales_code
      item.sales_stat_code = item.origin_sales_stat_code
      item.sales_date = item.origin_sales_date
      item.sales_text = item.origin_sales_text
      this.$forceUpdate()
    },
    editSet (item) {
      item.edit = false
      // eslint-disable-next-line no-undef
      item.sales_date = dateFormatToString(item.sales_date)
      item.login_luna_no = this.$store.getters.getUser.luna_no
      http
        .post('/sales/salesmod', {
          params: {
            sales_data: item
          }
        })
        .then(result => {
          var msg = '정상적으로 수정되었습니다.'
          if (result.data !== '0000') {
            msg = '처리중 문제가 발생했습니다.'
          }
          this.$modal.show(ModalAlert,
            {
              title: '영업내용 수정',
              text: msg
            }, {
              width: 450,
              height: 'auto'
            }
          )
          this.get()
        })
    },
    editDel (item) {
      this.$modal.show(
        ModalConfirm,
        {
          title: '영업항목 삭제',
          data: {
            url: '/sales/salesdel',
            item: {
              del_type: 'one', // all:전체삭제, one:개별삭제
              sales_no: item.sales_no,
              login_luna_no: this.$store.getters.getUser.luna_no
            },
            refresh: 'refreshSalesDetail'
          }
        }, {
          width: 450,
          height: 'auto'
        }
      )
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
    'lunaAccount'
  ]
}
</script>
