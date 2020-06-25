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
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <template v-if="!readonly">
                    <col style="width:10%"/>
                    </template>
                  </colgroup>
                  <thead>
                    <th>카테고리명</th>
                    <th>depth</th>
                    <th>대분류</th>
                    <th>중분류</th>
                    <th>소분류</th>
                    <th>세분류</th>
                    <template v-if="!readonly">
                    <th>수정</th>
                    </template>
                  </thead>
                  <tbody>
                    <template v-if="list.length > 0">
                    <template v-for="(item, idx) in list">
                      <tr :key = "item.id">
                          <td class="text-center"> {{item.category_name}}</td>
                          <td class="text-center">{{item.category_depth_no}}</td>
                          <td class="text-center">{{item.depth1_category_name }}

                          </td>
                          <td class="text-center">
                            <select v-model="item.depth2_category_no" @change="getDepthData(item, 'depth3', '', true)" style="width:90%"
                              :disabled="readonly">
                              <option value="0">선택하세요</option>
                              <template v-for="data in item.depth2Data" >
                                <option :value="data.category_no" :key="data.id" :data-smartcategoryno="data.store_category_no">
                                  {{data.category_name}}
                                </option>
                              </template>
                            </select>
                          </td>
                          <td class="text-center">
                            <select v-model="item.depth3_category_no" @change="getDepthData(item, 'depth4', '', true)" style="width:90%"
                              :disabled="readonly">
                              <option value="0">선택하세요</option>
                              <template v-for="data in item.depth3Data" >
                                <option :value="data.category_no" :key="data.id" :data-smartcategoryno="data.store_category_no">
                                  {{data.category_name}}
                                </option>
                              </template>
                            </select>
                          </td>
                          <td class="text-center">
                            <select v-model="item.depth4_category_no" @change="getDepthData(item, 'final', '', true)" style="width:90%"
                              :disabled="readonly">
                              <option value="0">선택하세요</option>
                              <template v-for="data in item.depth4Data" >
                                <option :value="data.category_no" :key="data.id" :data-smartcategoryno="data.store_category_no">
                                  {{data.category_name}}
                                </option>
                              </template>
                            </select>

                          </td>
                          <template v-if="!readonly">
                          <td>
                            <button type="button" class="btn_min" :id="idx + '_btn_category_change'"
                              @click="matchCategoryItem(item, idx)">변경</button>
                          </td>
                          </template>
                      </tr>
                    </template>
                    </template>
                    <tr v-else>
                      <td colspan="7" class="emptyTable">
                        해당하는 내용이 없습니다.
                      </td>
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
import { eventBus } from '@/main'
export default {
  name: 'ModalStoreCategory',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      list: []
    }
  },
  props: {
    'item': Object,
    'readonly': Boolean,
    'userMenuAuth': Object
  },
  mounted () {
    this.get()
  },
  methods: {

    get () {
      this.isLoading = true
      let url = '/smartstore/productmatch/store/categorylist'

      http
        .get(url, {
          params: { // query string
            category_no: this.item.smart_product_category_no
          }
        })
        .then(response => {
          this.isLoading = false
          this.list = response.data.data

          let listData = this.list
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
    matchCategoryItem (item, idx) {
      if (!this.userMenuAuth.modify_data) {
        return
      }

      let categoryNo = item.category_no
      let smartCategoryNo = 0

      if (parseInt(item.depth4_category_no) !== 0) {
        categoryNo = item.depth4_category_no
        smartCategoryNo = item.depth4_smart_category_no
      } else if (parseInt(item.depth3_category_no) !== 0) {
        categoryNo = item.depth3_category_no
        smartCategoryNo = item.depth3_smart_category_no
      } else if (parseInt(item.depth2_category_no) !== 0) {
        categoryNo = item.depth2_category_no
        smartCategoryNo = item.depth2_smart_category_no
      } else if (parseInt(item.depth1_category_no) !== 0) {
        categoryNo = item.depth1_category_no
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
      this.isLoading = true
      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/update/product/category/' + this.$store.state.searchParam.mall_id + '/' + this.item.smartstore_product_no
      let form = new FormData()
      form.append('mall_id', this.$store.state.searchParam.mall_id)
      form.append('hosting_no', this.$store.state.searchParam.hosting_no)
      form.append('category_no', categoryNo)
      form.append('product_no', this.item.product_no)
      form.append('smartstore_product_no', this.item.smartstore_product_no)
      form.append('user_no', this.userNo)
      form.append('user_code', 1)
      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          if (response.data.responseCode === 0) {
            this.$forceUpdate()
            this.$modal.show(ModalAlert,
              { title: '', text: '상품 카테고리를 변경하였습니다.' }, { width: 450, height: 'auto' }
            )
            eventBus.$emit('modalCategory')
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '카테고리 변경 실패' }, { width: 450, height: 'auto' }
            )
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
