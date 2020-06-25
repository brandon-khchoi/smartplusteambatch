<template>
  <table summary="스마트스토어 쇼핑몰명">
    <caption class="blind">스마트스토어 쇼핑몰명</caption>
    <colgroup>
        <col style="width:200px"/>
        <col />
        <template v-if="!propMallNo">
        <col />
        <col style="width:200px"/>
        <col />
        <col />
        </template>
    </colgroup>
    <tbody>
      <tr>
        <th>자사몰명</th>
        <td>
          <template v-if="propMallNo">
            <select v-model="searchParam.mall_id" style="width:300px" >
              <template v-for="item in productLinkServiceInfo" >
                <option :value="item.mall_id" :key="item.id" :data-mallno="item.mall_no" :data-calcode="item.cal_code" :data-hostingno="item.hosting_no" :data-mallname="item.mall_name">
                    {{item.mall_name}}
                </option>
              </template>
            </select>
          </template>
          <template v-else>
            <div class="vue-select">
              <v-select
                label="mall_name"
                :options="productLinkServiceInfo"
                @input="changeMallInfoVal"
                placeholder="업체명 검색"
              >
                <span slot="no-options" >
                  검색된 내용이 없습니다.
                </span>

                <template v-slot:option="option">

                    <div style="display:inline;">
                      {{ option.mall_name }}
                    </div>
                    <div style="float:right;">
                    <img v-if="option.hosting_no === 1" src="https://lunaplus.lunasoft.co.kr/images/lunachat/icon/cafe24/logo-button.png" alt="카페24"/>
                    <img v-if="option.hosting_no === 2" src="https://lunaplus.lunasoft.co.kr/images/lunachat/icon/makeshop/logo-button.png" alt="메이크샵"/>
                    </div>
                </template>
              </v-select>
            </div>
          </template>
        </td>
        <template v-if="!propMallNo">
          <td colspan="4">
          </td>
        </template>
      </tr>
    </tbody>
  </table>

</template>

<script>
/**
 * 공통 서비스 몰 검색 리스트
 */
// import ModalAlert from '@/components/common/modal/ModalAlert'
import http from '@/http-commons'
import { eventBus } from '@/main'
import 'vue-select/dist/vue-select.css'

export default {
  name: 'CommonServiceList',
  data () {
    return {
      userNo: this.$store.getters.getUser.luna_no,
      productLinkServiceInfo: [],
      initProductLinkServiceInfo: [],
      searchParam: {
        mall_id: '0',
        mall_no: 0,
        cal_code: 0,
        hosting_no: 0,
        mall_name: ''
      },
      hostingList: [
        {hosting_no: 0, hosting_name: '전체'},
        {hosting_no: 1, hosting_name: '카페24'},
        {hosting_no: 2, hosting_name: '메이크샵'}
      ]
    }
  },
  props: {
    'propMallNo': String | Number, // 한개만 서버에서 가지고 오고 싶을 경우
    'propMallNoQuery': String | Number // 여러 셀렉트 박스중 한개만 선택하고 싶은 경우
  },
  mounted () {
    this.getServiceList()
  },
  methods: {
    getServiceList () {
      let url = '/smartstore/servicelist'
      let mallNo = 0
      let paramData = null
      if (this.propMallNo === undefined) {
      } else { /** mall_no 로 검색시 */
        mallNo = this.propMallNo
        paramData = {
          mall_no: mallNo
        }
      }

      http
        .get(url, {params: paramData})
        .then(response => {
          this.productLinkServiceInfo = response.data

          if (this.propMallNoQuery) { // url 에 mall_no 가 있을 경우
            for (let idx in this.productLinkServiceInfo) {
              console.log(this.productLinkServiceInfo[idx])
              if (this.productLinkServiceInfo[idx].mall_no === this.propMallNoQuery) {
                this.searchParam.mall_id = this.productLinkServiceInfo[idx].mall_id
                this.searchParam.mall_no = this.productLinkServiceInfo[idx].mall_no
                this.searchParam.cal_code = this.productLinkServiceInfo[idx].cal_code
                this.searchParam.hosting_no = this.productLinkServiceInfo[idx].hosting_no
                this.searchParam.mall_name = this.productLinkServiceInfo[idx].mall_name
                this.$store.state.searchParam = this.searchParam
                break
              }
            }
          }

          /** mall_no 로 검색시 */
          if (this.propMallNo !== undefined) {
            this.searchParam.mall_id = this.productLinkServiceInfo[0].mall_id
            this.searchParam.mall_no = this.productLinkServiceInfo[0].mall_no
            this.searchParam.cal_code = this.productLinkServiceInfo[0].cal_code
            this.searchParam.hosting_no = this.productLinkServiceInfo[0].hosting_no
            this.searchParam.mall_name = this.productLinkServiceInfo[0].mall_name
            this.$store.state.searchParam = this.searchParam
          }
          this.$store.state.searchParam = this.searchParam // state 초기화
          this.initProductLinkServiceInfo = this.productLinkServiceInfo // backup
        })
        .catch(e => {
          // console.log(e)
          // this.$modal.show(ModalAlert,
          //   { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          // )
        })
    },
    changeMallInfoVal (value) {
      if (value === null) {
        this.searchParam = {
          mall_id: '0',
          mall_no: 0,
          cal_code: 0,
          hosting_no: 0,
          mall_name: ''
        }
      } else {
        let mallId = value.mall_id
        let mallNo = parseInt(value.mall_no)
        let calCode = parseInt(value.cal_code)
        let hostingNo = parseInt(value.hosting_no)
        let mallName = value.mall_name
        this.searchParam.mall_no = mallNo
        this.searchParam.mall_id = mallId
        this.searchParam.cal_code = calCode
        this.searchParam.hosting_no = hostingNo
        this.searchParam.mall_name = mallName
      }
      this.$store.state.searchParam = this.searchParam
      eventBus.$emit('commonServiceListChange')
      // data - mall - no
    }
  }
}

</script>

<!-- <style src="vue-multiselect/dist/vue-multiselect.min.css"></style> -->

<style scoped>

.vue-select >>> .vs__dropdown-toggle {
    border-radius: 0px;
    height: 36px;
}
</style>
