<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          상품명 : {{ item.product_name }} [카테고리]
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="tableWrap tableList tableHover">
              <table>
                  <colgroup>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:60px;"/>
                    <template v-if="whereOpen !== 'MallProductList'">
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:65px;"/>
                    <col style="width:125px;"/>
                    </template>
                  </colgroup>
                  <thead>
                    <tr v-if="whereOpen !== 'MallProductList'">
                      <th colspan="5">자사몰</th>
                      <th colspan="4">스마트스토어</th>
                      <th colspan="2">매칭</th>
                    </tr>
                    <tr>
                      <th>대분류</th>
                      <th>중분류</th>
                      <th>소분류</th>
                      <th>세분류</th>
                      <th>사용여부</th>
                      <template v-if="whereOpen !== 'MallProductList'">
                      <th>대분류</th>
                      <th>중분류</th>
                      <th>소분류</th>
                      <th>세분류</th>
                      <th>매칭상태</th>
                      <th>매칭하기</th>
                      </template>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-if="list.length > 0">
                    <template v-for="(item, idx) in list">
                      <tr :key = "item.id" :style="[
                                                    item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' ) ? { 'background': '#f1f1f1'} : {},
                                                    isToMatch(item, idx) ? { 'border': '2px solid red'} : {}
                                                    ]">
                          <td class="textCenter" v-html="item.first_depth"></td>
                          <td class="text-center">
                            <template v-if="item.second_depth">
                            {{item.second_depth}}
                            </template>
                            <template v-else>
                              -
                            </template>
                          </td>
                          <td class="text-center">
                            <template v-if="item.third_depth">
                              {{item.third_depth}}
                            </template>
                            <template v-else>
                              -
                            </template>
                            </td>
                          <td class="text-center">
                            <template v-if="item.last_depth">
                              {{item.last_depth}}
                            </template>
                            <template v-else>
                              -
                            </template>
                            </td>
                          <td class="text-center">
                            <template v-if="item.use_yn === 1">
                              사용
                            </template>
                            <template v-else>
                              미사용
                            </template>
                          </td>
                          <template v-if="whereOpen !== 'MallProductList'">
                          <td class="textCenter">
                            <template v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" style="width:90%">
                              -
                            </template>
                            <template v-else-if="item.store_category_no && depthData" style="width:90%">
                              {{getStrSelectData(depthData, item.depth1_category_no)}}
                            </template>
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
                            <template v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" disabled style="width:90%">
                              -
                            </template>
                            <template v-else-if="item.store_category_no" style="width:90%">
                              {{getStrSelectData(item.depth2Data, item.depth2_category_no)}}
                            </template>
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
                            <template v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" disabled style="width:90%">
                              -
                            </template>
                            <template v-else-if="item.store_category_no" style="width:90%">
                              {{getStrSelectData(item.depth3Data, item.depth3_category_no)}}
                            </template>
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
                            <template v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )" disabled style="width:90%">
                              -
                            </template>
                            <template v-else-if="item.store_category_no" style="width:90%">
                              {{getStrSelectData(item.depth4Data, item.depth4_category_no)}}
                            </template>
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
                            <template v-if="item.store_category_no" >
                              매칭완료
                            </template>
                            <template v-else>
                                <template v-if="item.store_category_stat_code === 2">
                                  <span style="color:#3cb878;">PASS</span>
                                </template>
                                <template v-else>
                                  <template v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )">

                                  </template>
                                  <template v-else>
                                    <span style="color:#e72b4b;">미매칭</span>
                                  </template>
                                </template>
                            </template>
                          </td>
                          <td class="textCenter">
                            <template v-if="item.category_depth_no === 1 && !(item.category_type_code === 'O' || item.category_type_code === 'P' )"> <!-- 1뎁스만 있는 경우 버튼 없음-->
                            </template>
                            <template v-else>
                              <template v-if="item.store_category_no" >
                                  -
                              </template>
                              <template v-else>
                                  <button v-if="item.store_category_stat_code === 2"
                                    type="button" class="btn_min" :id="idx + '_btn'" @click="matchCategoryItem(item, idx,'update')"
                                    style="border: 1px solid #e72b4b;color: #e72b4b;">매칭하기
                                  </button>
                                  <button v-else
                                    type="button" class="btn_min" :id="idx + '_btn'" @click="matchCategoryItem(item, idx,'insert')"
                                    style="border: 1px solid #e72b4b;color: #e72b4b;">매칭하기
                                  </button>
                              </template>

                            </template>
                          </td>
                          </template> <!-- 스마트스토어 매칭 카테고리 -->
                      </tr>
                    </template>
                    </template>
                    <tr v-else>
                      <template v-if="whereOpen !== 'MallProductList'">
                      <td colspan="11" class="emptyTable">
                        해당하는 내용이 없습니다.
                      </td>
                      </template>
                      <template v-else>
                      <td colspan="5" class="emptyTable">
                        해당하는 내용이 없습니다.
                      </td>
                      </template>
                    </tr>
                  </tbody>
              </table>
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
export default {
  name: 'ModalCategory',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      userCode: 1,
      list: [],
      depthData: [],
      isFirstCategoryNo: Infinity
    }
  },
  props: {
    'item': Object,
    'whereOpen': String,
    'userMenuAuth': Object
  },
  mounted () {
    this.get()
    this.getCategoryDepth()
  },
  watch: {
    'list': {
      deep: true,
      handler (newVal, oldVal) {
        console.log(newVal, 'newVal')

        if (newVal !== undefined && newVal.length > 0) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          let dataList = this.list
          let isFirstCategoryNo = this.isFirstCategoryNo
          // let selected = this.selected
          newVal.filter(function (p, idx) {
            return Object.keys(p).some(function (prop) {
              if (prop === 'store_category_no') { // 변경되었던 데이터만 changed 라는 객체로 잇어서 changed 가 있는 데이터들만 삭제
                /** 대분류가 아니거나 대분류이면 O와 P타입 매칭  */
                if (dataList[idx]['category_depth_no'] !== 1 || (dataList[idx]['category_type_code'] === 'O' || dataList[idx]['category_type_code'] === 'P')) {
                  if (isFirstCategoryNo > dataList[idx]['category_no']) {
                    isFirstCategoryNo = dataList[idx]['category_no']
                  }
                }
              } else {
              }
            })
          })
          this.isFirstCategoryNo = isFirstCategoryNo
          // Log it
        }
      }
    }
  },
  computed: {
  },
  methods: {

    get () {
      console.log(this.item.product_category_no)
      this.isLoading = true
      let url = '/smartstore/productmatch/categorylist'

      http
        .get(url, {
          params: { // query string
            mall_id: this.$store.state.searchParam.mall_id,
            mall_no: this.$store.state.searchParam.mall_no,
            cal_code: this.$store.state.searchParam.cal_code,
            category_no: encodeURI(this.item.product_category_no)
          }
        })
        .then(response => {
          this.isLoading = false
          this.list = response.data.data

          let listData = this.list
          for (let index in listData) {
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
    },
    isToMatch (item, idx) {
      // console.log(this.whereOpen)
      if (this.whereOpen === 'MallProductList') {
        return false
      }
      if (item.category_no === this.isFirstCategoryNo && item.store_category_stat_code === 1) {
        return true
      }
      return false
    },
    /**
     * select 박스에서 선택된 카테고리 명
     */
    getStrSelectData (arrayData, storeCategoryNo) {
      if (!arrayData) {
        return
      }
      for (const item of arrayData) {
        if (item.category_no === storeCategoryNo) {
          return item.category_name
        }
      }
      return '-'
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
    matchCategoryItem (item, idx, part, statCode) {
      console.log('matchCategoryItem')
      let url = ''
      if (part === 'update') {
        url = '/smartstore/matchcategory/update'
        if (!this.userMenuAuth.modify_data) return
      } else if (part === 'insert') {
        url = '/smartstore/matchcategory/insert'
        if (!this.userMenuAuth.create_data) return
      } else {
        url = '/smartstore/matchcategory/delete'
        if (!this.userMenuAuth.delete_data) return
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
          if (part === 'delete') {
            item.store_category_no = null // 업데이트시 버튼 리렌더링때문에 추가, store_category_no 는 버튼렌더 조건으로만 사용
            item.depth1_category_no = '0'
            item.depth2_category_no = '0'
            item.depth2Data = []
            item.depth3_category_no = '0'
            item.depth3Data = []
            item.depth4_category_no = '0'
            item.depth4Data = []
            this.$forceUpdate()
            this.$modal.show(ModalAlert,
              { title: '', text: '매칭을 취소했습니다.' }, { width: 450, height: 'auto' }
            )
          } else if (response.data.responseCode === 0) {
            item.store_category_no = response.data.data // 업데이트시 버튼 리렌더링때문에 추가, store_category_no 는 버튼렌더 조건으로만 사용
            item.store_category_stat_code = 1 // PASS 업데이트
            this.$forceUpdate()
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
    matchCategoryDeleteAndPass (item, idx) {
      let url = '/smartstore/matchcategory/deletepass'

      if (!this.userMenuAuth.create_data) return

      let categoryNo = item.category_no
      let form = new FormData()
      form.append('mall_no', this.$store.state.searchParam.mall_no)
      form.append('category_no', categoryNo)
      form.append('user_no', this.userNo)
      form.append('user_code', 1)
      this.isLoading = true
      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          if (response.data.responseCode === 0) {
            item.store_category_no = null // 업데이트시 버튼 리렌더링때문에 추가, store_category_no 는 버튼렌더 조건으로만 사용
            item.depth1_category_no = '0'
            item.depth2_category_no = '0'
            item.depth2Data = []
            item.depth3_category_no = '0'
            item.depth3Data = []
            item.depth4_category_no = '0'
            item.depth4Data = []
            item.store_category_stat_code = 2
            this.$forceUpdate()
            this.$modal.show(ModalAlert,
              { title: '', text: '매칭패스 하였습니다.' }, { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          // console.log(e)
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
  }
}
</script>
<style scoped>
.vue-modal-box .dialog-content .dialog-c-text {
    font-size: 12px;
    padding: 10px;
}
.vue-modal-box .dialog-content table td {
    padding-left: 2px;
    padding-right: 2px;
}
</style>
