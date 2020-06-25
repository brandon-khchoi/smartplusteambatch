<template>
  <div class="content">
    <h1>판매자 연동 설정</h1>
    <div class="formContainer">
      <div class="searchWrap">
        <CommonServiceList :propMallNoQuery= $route.query.mall_no></CommonServiceList>
        <div class="btnWrap">
          <ul>
            <li>
              <p class="btn_search" @click="get()"><i class="fas fa-search"></i>검색</p>
            </li>
          </ul>
        </div>
      </div>
      <div class="loading">
        <article class="searchResult" >
          <div class="articleWrap">
            <div class="tableTopWrap">
              <p class="total" v-if="list.data">결과 : 총 <span> {{list.data.length}} </span> 건</p>
              <div class="btnWrap">
                <ul>
                  <li>
                    <p class="btn_add" v-if="userMenuAuth.create_data">
                      <router-link :to="{ name: 'sellercreate'}"><i class="fas fa-plus" ></i>등록</router-link>
                    </p>
                    <p class="btn_add" v-else>
                      <i class="fas fa-plus" ></i>등록
                    </p>
                  </li>
                </ul>
              </div>
            </div>
            <div class="tableWrap tableList tableGroup">
              <table summary="판매자 연동 설정">
                <caption class="blind">판매자 연동 설정</caption>
                <colgroup>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                </colgroup>
                <thead>
                <tr>
                  <th >자사몰명</th>
                  <th >연동 쇼핑몰명</th>
                  <th >A/S 전화번호</th>
                  <th >API 판매자 ID</th>
                  <th >API 키</th>
                  <th >자사몰 상품 등록 기준일</th>
                  <th >초기 연동시 상품가격 설정</th>
                  <th >자동 연동 사용여부</th>
                  <th >조회</th>
                </tr>
                </thead>
                <tbody>
                  <template v-if="list.data.length > 0">
                  <template v-for="(item, idx) in list.data">
                  <tr :key="item.id">

                    <td class="textCenter">{{list.mall_name}}</td>
                    <td class="textCenter">
                      <template v-if="item.store_no === 1">
                        스마트스토어
                      </template>
                      <template v-else>
                        기타업체
                      </template>
                      </td>
                    <td class="textCenter">{{item.mall_as_tel_no}}</td>
                    <td class="textCenter">{{item.api_sell_id}}</td>
                    <td class="textCenter">{{item.api_key}}</td>
                    <td class="textCenter">
                      <template v-if="item.product_standard_register_date">
                        {{item.product_standard_register_date}}
                      </template>
                      <template v-else>
                        없음
                      </template>
                    </td>
                    <td class="textCenter">
                      <template v-if="item.product_init_price_type_code === 1">
                        판매가 적용
                      </template>
                      <template v-if="item.product_init_price_type_code === 2">
                        할인가 적용
                      </template>
                    </td>
                    <td class="textCenter">
                      <template v-if="item.use_yn === 1">
                        사용중
                      </template>
                      <template v-else>
                        미사용
                      </template>
                    </td>
                    <td class="textCenter">
                      <button type="button" class="btn_min" :id="idx + '_btn'" v-if="userMenuAuth.retrieve_detail" @click="linkUpdate(item, idx)">조회</button>
                      <button type="button" class="btn_min" :id="idx + '_btn'" v-else >조회</button>
                    </td>
                  </tr>
                  </template>
                  </template>
                  <template v-else>
                    <tr >
                      <td colspan="9" class="textCenter" >
                        조회된 내용이 없습니다.
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>

          </div>
        </article>
      </div>
    </div>
    <v-dialog />
    <Spinner v-if="isLoading" />
  </div>

</template>

<script>

import ModalAlert from '@/components/common/modal/ModalAlert'
import CommonServiceList from './CommonServiceList'
import { eventBus } from '@/main'
import http from '@/http-commons'
export default {
  name: 'SellerList',
  components: {
    CommonServiceList
  },
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      list: {
        data: [],
        mall_name: ''
      }
    }
  },
  created () {

  },
  mounted () {
    if (this.$route.query.mall_no) {
      if (this.$store.state.searchParam) { // 파라미터 쿼리 있을 경우 자동 검색
        this.get()
      }
    }
    eventBus.$on('commonServiceListChange', () => {
      this.get()
    })
  },
  beforeDestroy () {
    eventBus.$off('commonServiceListChange')
  },
  methods: {
    get () {
      if (this.userMenuAuth.retrieve_list) {
        console.log(this.$store.state.searchParam)
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
          return
        }

        this.isLoading = true
        let url = '/smartstore/sellerregister/list'
        http
          .get(url, {
            params: { // query string
              mall_no: this.$store.state.searchParam.mall_no
            }
          })
          .then(response => {
            this.list = response.data
            this.list.mall_name = this.$store.state.searchParam.mall_name
            this.isLoading = false
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
      }
    },
    linkUpdate (item) {
      this.$router.push({name: 'sellerupdate', params: {mall_id: this.$store.state.searchParam.mall_id, mall_no: item.mall_no, store_no: item.store_no}})
    }
  },
  props: {
    'userMenuAuth': Object
  }
}

</script>
