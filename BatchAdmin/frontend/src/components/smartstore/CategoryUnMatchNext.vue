<template>
  <div class="content">
    <h1>미매칭 카테고리 </h1>
    <div class="formContainer">
      <div class="searchWrap">
        <CommonServiceList></CommonServiceList>
        <div class="btnWrap">
          <ul>
            <li>
              <p class="btn_search" @click="get()"><i class="fas fa-search"></i>검색</p>
            </li>
          </ul>
        </div>
      </div>
        <article class="searchResult" >
          <div class="articleWrap">
            <div class="tableTopWrap">
              <p class="total">결과 : 총 <span> {{list.total_cnt}} </span> 건</p>
              <div class="btnWrap">
                <ul>
                  <li>
                    <p class="btn_add" >
                      <router-link :to="{ name: 'productlist'}">NEXT</router-link>
                    </p>
                  </li>
                </ul>
              </div>
            </div>
            <div class="tableWrap tableList tableGroup tableHover">
              <table summary="미매칭 카테고리">
                <caption class="blind">미매칭 카테고리</caption>
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
                </colgroup>
                <thead>
                <tr>
                  <th colspan="4">자사몰</th>
                  <th colspan="4">스마트스토어</th>
                  <th colspan="2">매칭</th>
                </tr>
                <tr>
                  <th >대분류</th>
                  <th >중분류</th>
                  <th >소분류</th>
                  <th >세분류</th>
                  <th >대분류</th>
                  <th >중분류</th>
                  <th >소분류</th>
                  <th >세분류</th>
                  <th >매칭하기</th>
                  <th >매칭패스</th>
                </tr>
                </thead>
                <tbody>
                  <template v-if="list.total_cnt">
                  <template v-for="(item, idx) in list.data">
                  <tr :key="item.id" :style="[item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )? { 'background': '#f1f1f1'} : {}]">
                    <td class="textCenter" v-html="item.first_depth"></td>
                    <td class="textCenter" v-html="item.second_depth"></td>
                    <td class="textCenter" v-html="item.third_depth"></td>
                    <td class="textCenter" v-html="item.last_depth"></td>
                    <td class="textCenter">
                      <select v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" disabled style="width:90%">
                        <option value="0">선택하세요</option>
                      </select>
                      <select v-else v-model="item.depth1_category_no" @change="getDepthData(item, 'depth2', '', true)" style="width:90%" >
                        <option value="0">선택하세요</option>
                        <template v-for="data in depthData" >
                          <option :value="data.category_no" :key="data.id" :data-smartcategoryno="data.store_category_no">
                            {{data.category_name}}
                          </option>
                        </template>
                      </select>
                    </td>
                    <td class="textCenter">
                      <select v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" disabled style="width:90%">
                        <option value="0">선택하세요</option>
                      </select>
                      <select v-else v-model="item.depth2_category_no" @change="getDepthData(item, 'depth3', '', true)" style="width:90%">
                        <option value="0">선택하세요</option>
                        <template v-for="data in item.depth2Data" >
                          <option :value="data.category_no" :key="data.id" :data-smartcategoryno="data.store_category_no">
                            {{data.category_name}}
                          </option>
                        </template>
                      </select>
                    </td>
                    <td class="textCenter">
                      <select v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" disabled style="width:90%">
                        <option value="0">선택하세요</option>
                      </select>
                      <select v-else v-model="item.depth3_category_no" @change="getDepthData(item, 'depth4', '', true)" style="width:90%">
                        <option value="0">선택하세요</option>
                        <template v-for="data in item.depth3Data" >
                          <option :value="data.category_no" :key="data.id" :data-smartcategoryno="data.store_category_no">
                            {{data.category_name}}
                          </option>
                        </template>
                      </select>
                    </td>
                    <td class="textCenter">
                      <select v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" disabled style="width:90%">
                        <option value="0">선택하세요</option>
                      </select>
                      <select v-else v-model="item.depth4_category_no" @change="getDepthData(item, 'final', '', true)" style="width:90%">
                        <option value="0">선택하세요</option>
                        <template v-for="data in item.depth4Data" >
                          <option :value="data.category_no" :key="data.id" :data-smartcategoryno="data.store_category_no">
                            {{data.category_name}}
                          </option>
                        </template>
                      </select>
                    </td>
                    <td class="textCenter">
                      <template v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )"> <!-- 1뎁스만 있는 경우 버튼 없음-->
                      </template>
                      <template v-else>
                        <template v-if="item.store_category_no" >
                          <div :id="idx + '_btn_outer'">
                            <button type="button" class="btn_min" :id="idx + '_btn'" @click="matchCategoryItem(item, idx,'update')">매칭수정</button>
                          </div>
                        </template>
                        <template v-else>
                          <div :id="idx + '_btn_outer'">
                            <button type="button" class="btn_min" :id="idx + '_btn'" @click="matchCategoryItem(item, idx,'insert')"
                            style="border: 1px solid #e72b4b;color: #e72b4b;">매칭하기</button>
                          </div>
                        </template>
                      </template>
                    </td>
                    <td class="textCenter">
                      <div :id="idx + '_btn_outer'">
                        <button type="button" class="btn_min" :id="idx + '_btn'" @click="matchCategoryPass(item, idx)">pass</button>
                      </div>
                    </td>
                  </tr>
                  </template>
                  </template>
                  <template v-else>
                    <tr >
                      <td colspan="10" class="textCenter" >
                        조회된 내용이 없습니다.
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>

            <Pagination
              :paginate="list.paginate"
              :action="get"
              :isPropPageCntShow="true"
            ></Pagination>

          </div>
        </article>
    </div>
    <v-dialog />
    <Spinner v-if="isLoading" />
  </div>

</template>

<script>

import Pagination from '@/components/common/Pagination'
import ModalAlert from '@/components/common/modal/ModalAlert'
import CommonServiceList from './CommonServiceList'
import { eventBus } from '@/main'
import http from '@/http-commons'

export default {
  name: 'CategoryUnMatchNext',
  components: {
    Pagination,
    CommonServiceList
  },
  data () {
    return {
      isLoading: false,
      userCode: 1,
      userNo: this.$store.getters.getUser.luna_no,
      list: {
        total_cnt: 0,
        data: []
      },
      depthData: []
    }
  },
  watch: {
    'listSize': {
      deep: true,
      handler (newVal, oldVal) {
        if (oldVal !== undefined) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          console.log('length')
          if (newVal === 0 && this.list.total_cnt === 0) {
            this.$router.push({name: 'productlist', query: { mall_no: this.$store.state.searchParam.mall_no }})
          }
          // Log it
        }
      }
    }
  },
  computed: {
    listSize: function () {
      return this.list.data.length
    }
  },
  created () {
  },
  mounted () {
    this.getCategoryDepth()
    eventBus.$on('commonServiceListChange', () => {
      this.get()
    })
  },
  beforeDestroy () {
    eventBus.$off('commonServiceListChange')
  },
  methods: {
    get (current_page = 1, per_page = 20) {
      if (this.userMenuAuth.retrieve_list) {
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
          return
        }

        this.isLoading = true
        let url = '/smartstore/mallcategory/unmatch'
        http
          .get(url, {
            params: { // query string
              mall_id: this.$store.state.searchParam.mall_id,
              mall_no: this.$store.state.searchParam.mall_no,
              cal_code: this.$store.state.searchParam.cal_code,
              per_page: per_page,
              current_page: current_page
            }
          })
          .then(response => {
            this.isLoading = false
            this.list = response.data
            // eslint-disable-next-line no-undef
            this.list.paginate = makePaginate(response.data); this.list.max = pageMaxNumber(response.data)
            let listData = this.list.data
            for (let index in listData) {
              // console.log(listData[index])
              if (listData[index].depth1_category_no !== '0') {
                this.getDepthData(listData[index], 'depth2', 1, false)
              }
              if (listData[index].depth2_category_no !== '0') {
                this.getDepthData(listData[index], 'depth3', 2, false)
              }
              if (listData[index].depth3_category_no !== '0') {
                this.getDepthData(listData[index], 'depth4', 3, false)
              }
              if (listData[index].depth4_category_no !== '0') {
                this.getDepthData(listData[index], 'final', 4, false)
              }
            }
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
    /**
     * item : row data
     * targetName : 변경하고자 하는 depth data
     * currentDepNo : 현재 category depth 레벨
     * selectYn : select 박스로 변경했는지 여부
     */
    getDepthData (item, targetName, currentDepNo, selectYn) {
      let categoryNo = '' // 스마트 스토어에서 가져온 키 값(외부업체 키 값)
      let smartCategoryNo = '' // 스마트 스토어 db 의 pk(우리 키 값)

      if (selectYn) {
        categoryNo = event.target.value
        smartCategoryNo = event.target[event.target.selectedIndex].dataset.smartcategoryno
      } else { // list 바인딩시 호출
        if (currentDepNo === 1) {
          categoryNo = item.depth1_category_no
          smartCategoryNo = item.store_category_no
        } else if (currentDepNo === 2) {
          categoryNo = item.depth2_category_no
          smartCategoryNo = item.store_category_no
        } else if (currentDepNo === 3) {
          categoryNo = item.depth3_category_no
          smartCategoryNo = item.store_category_no
        } else if (currentDepNo === 4) {
          categoryNo = item.depth4_category_no
          smartCategoryNo = item.store_category_no
        }
      }

      if (targetName === 'depth2') {
        item.depth1_smart_category_no = smartCategoryNo
      } else if (targetName === 'depth3') {
        item.depth2_smart_category_no = smartCategoryNo
      } else if (targetName === 'depth4') {
        item.depth3_smart_category_no = smartCategoryNo
      } else if (targetName === 'final') {
        item.depth4_smart_category_no = smartCategoryNo
      }

      let url = '/smartstore/categorydepthbyno'
      http
        .get(url, {
          params: { // query string
            category_no: categoryNo
          }
        })
        .then(response => {
          if (selectYn) {
            if (targetName === 'depth2') {
              item.depth2Data = response.data
              item.depth2_category_no = '0' // 뒤에 있는 select box 초기화
              item.depth3_category_no = '0'
              item.depth4_category_no = '0'
            } else if (targetName === 'depth3') {
              item.depth3Data = response.data
              item.depth3_category_no = '0'
              item.depth4_category_no = '0'
            } else if (targetName === 'depth4') {
              item.depth4Data = response.data
              item.depth4_category_no = '0'
            }
          } else {
            if (targetName === 'depth2') {
              item.depth2Data = response.data
            } else if (targetName === 'depth3') {
              item.depth3Data = response.data
            } else if (targetName === 'depth4') {
              item.depth4Data = response.data
            }
          }

          this.$forceUpdate() // 없으면 하위 depth 데이터가 화면에 바로 반영 안됨
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    getCategoryDepth () {
      let url = '/smartstore/categorydepth'

      http
        .get(url)
        .then(response => {
          this.depthData = response.data
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    matchCategoryItem (item, idx, part) {
      let url = ''
      if (part === 'update') {
        url = '/smartstore/matchcategory/update'
        if (!this.userMenuAuth.modify_data) return
      } else {
        url = '/smartstore/matchcategory/insert'
        if (!this.userMenuAuth.create_data) return
      }
      let categoryNo = item.category_no
      let smartCategoryNo = 0

      if (parseInt(item.depth4_category_no) !== 0) {
        smartCategoryNo = item.depth4_smart_category_no
      } else if (parseInt(item.depth3_category_no) !== 0) {
        smartCategoryNo = item.depth3_smart_category_no
      } else if (parseInt(item.depth2_category_no) !== 0) {
        smartCategoryNo = item.depth2_smart_category_no
      } else if (parseInt(item.depth1_category_no) !== 0) {
        smartCategoryNo = item.depth1_smart_category_no
      }
      if (smartCategoryNo === 0) {
        this.$modal.show(ModalAlert,
          { title: '', text: '스마트스토어 카테고리를 선택해주세요' }, { width: 450, height: 'auto' }
        )
        return
      }
      // validation 체크
      if (!this.validaionCheck(item)) return

      // validation 체크 끝
      // console.log(smartCategoryNo)
      let form = new FormData()
      form.append('mall_no', this.$store.state.searchParam.mall_no)
      form.append('category_no', categoryNo)
      form.append('store_category_no', smartCategoryNo)
      form.append('user_no', this.userNo)
      form.append('user_code', this.userCode)
      this.isLoading = true
      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          if (response.data.responseCode === 0) {
            this.list.data.splice(idx, 1) // 리스트에서 삭제
            this.list.total_cnt -= 1
            this.$modal.show(ModalAlert,
              { title: '', text: '매칭하였습니다.' }, { width: 450, height: 'auto' }
            )
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '매칭 실패' }, { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          this.isLoading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    matchCategoryPass (item, idx) {
      let url = '/smartstore/matchcategory/pass'

      if (!this.userMenuAuth.create_data) return

      let categoryNo = item.category_no
      let form = new FormData()
      form.append('mall_no', this.$store.state.searchParam.mall_no)
      form.append('category_no', categoryNo)
      form.append('user_no', this.userNo)
      form.append('user_code', this.userCode)
      this.isLoading = true
      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          if (response.data.responseCode === 0) {
            this.list.data.splice(idx, 1) // 리스트에서 삭제
            this.list.total_cnt -= 1
            this.$modal.show(ModalAlert,
              { title: '', text: '매칭패스 하였습니다.' }, { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          this.isLoading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    validaionCheck (item) {
      if (item.depth2Data !== undefined && item.depth2Data.length > 0) {
        if (item.depth2_category_no === '0') {
          this.$modal.show(ModalAlert,
            { title: '', text: '중분류를 선택하지 않았습니다.' }, { width: 450, height: 'auto' }
          )
          return false
        }
      }
      if (item.depth3Data !== undefined && item.depth3Data.length > 0) {
        if (item.depth3_category_no === '0') {
          this.$modal.show(ModalAlert,
            { title: '', text: '소분류를 선택하지 않았습니다.' }, { width: 450, height: 'auto' }
          )
          return false
        }
      }
      if (item.depth4Data !== undefined && item.depth4Data.length > 0) {
        if (item.depth4_category_no === '0') {
          this.$modal.show(ModalAlert,
            { title: '', text: '세분류를 선택하지 않았습니다.' }, { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return true
    }
  },
  props: {
    'userMenuAuth': Object
  }
}

</script>
