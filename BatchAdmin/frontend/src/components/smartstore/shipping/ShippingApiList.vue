<template>
  <div class="content">
    <h1>출고/배송지 관리</h1>
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
                    <template v-if="list.data.length == 0">
                    <p class="btn_add" v-if="userMenuAuth.create_data">
                      <router-link :to="{ name: 'shippingapicreate'}"><i class="fas fa-plus" ></i>등록</router-link>
                    </p>
                    <p class="btn_add" v-else>
                      <i class="fas fa-plus" ></i>등록
                    </p>
                    </template>
                  </li>
                </ul>
              </div>
            </div>
            <div class="tableWrap tableList tableGroup">
              <table summary="출고/배송지 리스트">
                <caption class="blind">출고/배송지 리스트</caption>
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
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                </colgroup>
                <thead>
                <tr>
                  <th >자사몰명</th>
                  <th >배송지명</th>
                  <th >대표배송지 여부</th>
                  <th >우편번호</th>
                  <th >주소</th>
                  <th >주소 상세</th>
                  <th >전화번호</th>
                  <th >무료 배송 기준</th>
                  <th >기본배송비</th>
                  <th >반품배송비</th>
                  <th >교환배송비</th>
                  <th >조회</th>
                </tr>
                </thead>
                <tbody>
                  <template v-if="list.data.length > 0">
                  <template v-for="(item, idx) in list.data">
                  <tr :key="item.id">

                    <td class="textCenter">{{list.mall_name}}</td>
                    <td class="textCenter">{{item.delivery_name}}</td>
                    <td class="textCenter">
                      <template v-if="item.delivery_release_default_yn == 1">
                        대표 출고지
                      </template>
                      <template v-if="item.delivery_release_default_yn == 1 && item.delivery_return_default_yn == 1">
                        <br>
                      </template>
                      <template v-if="item.delivery_return_default_yn == 1">
                        대표 반품/교환지
                      </template>
                      </td>
                    <td class="textCenter">{{item.delivery_zipcode_no}}</td>
                    <td class="textCenter">{{item.delivery_addr}}</td>
                    <td class="textCenter">{{item.delivery_detail_addr}}</td>
                    <td class="textCenter">{{item.delivery_tel_no}}</td>
                    <td class="textCenter">
                      <template v-if="item.delivery_free_price">
                      {{item.delivery_free_price | currency}}원
                      </template>
                      <template v-else>
                          -
                      </template>
                    </td>
                    <td class="textCenter">
                      <template v-if="item.delivery_default_price">
                      {{item.delivery_default_price | currency}}원
                      </template>
                      <template v-else>
                          -
                      </template>
                    </td>
                    <td class="textCenter">
                      <template v-if="item.delivery_return_price">
                      {{item.delivery_return_price | currency}}원
                      </template>
                      <template v-else>
                          -
                      </template>
                    </td>
                    <td class="textCenter">
                      <template v-if="item.delivery_exchange_price">
                      {{item.delivery_exchange_price | currency}}원
                      </template>
                      <template v-else>
                          -
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
                      <td colspan="12" class="textCenter" >
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
import CommonServiceList from '../CommonServiceList'
import { eventBus } from '@/main'
import http from '@/http-commons'
export default {
  name: 'ShippingApiList',
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
      console.log(this.$store.state)
      if (this.$store.state.searchParam) {
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
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
          return
        }

        this.isLoading = true
        let url = '/smartstore/shipping/api/list'
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
      console.log(item)
      if (item.delivery_release_default_yn === 1) { // 출고배송지 탭
        this.$router.push({name: 'shippingapiupdate', params: {mall_no: item.mall_no, tab_name: 'release'}})
      } else if (item.delivery_return_default_yn === 1) { // 교환/반품지 탭
        this.$router.push({name: 'shippingapiupdate', params: {mall_no: item.mall_no, tab_name: 'return'}})
      }
    }
  },
  props: {
    'userMenuAuth': Object
  }
}

</script>
