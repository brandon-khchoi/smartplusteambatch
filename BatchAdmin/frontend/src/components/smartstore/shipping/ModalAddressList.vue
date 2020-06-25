<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          판매자 주소지 목록
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="popupContentWrap">
            <div class="popupContent">
                <div class="tableWrap tableList tableHover" >
                    <table>
                        <colgroup>
                          <col style="width:10%"/>
                          <col style="width:15%"/>
                          <col style="width:10%"/>
                          <col style="width:20%"/>
                          <col style="width:10%"/>
                          <col style="width:10%"/>
                        </colgroup>
                        <thead>
                          <th>주소ID</th>
                          <th>주소록유형코드</th>
                          <th>주소록명</th>
                          <th>주소</th>
                          <th>전화번호1</th>
                          <th>선택</th>
                        </thead>
                        <tbody>
                          <template v-if="list.length > 0">
                          <template v-for="item in list">
                            <!-- <tr :key = "item.id"
                              :style="[ !(item.addressType === '00400'  && whereOpen === 'ShippingApiCreateRelease')
                                      && !(item.addressType === '00500'  && whereOpen === 'ShippingApiCreateReturn')  ?
                                      { 'background': '#f1f1f1'} : {}]"> -->
                            <tr :key = "item.id">
                                <td class="text-center">
                                  {{item.addressId}}
                                </td>
                                <td class="text-center">
                                  {{item.addressType}}
                                  <template v-if="item.addressType === '00300'">
                                    <br>[일반 주소]

                                  </template>
                                  <template v-if="item.addressType === '00400'">
                                    <br>[대표 출고지 주소]
                                  </template>
                                  <template v-if="item.addressType === '00500'">
                                    <br>[대표 반품/교환지 주소]
                                  </template>
                                </td>
                                <td class="text-center">
                                  {{item.name}}
                                </td>
                                <td class="text-center">
                                  {{item.fullAddress}}
                                </td>
                                <td class="text-center">
                                  {{item.phoneNumber1}}
                                </td>
                                <td class="textCenter">
                                  <button type="button" class="btn_min" @click="matchAddress(item)">선택</button>
                                  <!-- <template v-if="(item.addressType === '00400'  && whereOpen === 'ShippingApiCreateRelease')">
                                    <button type="button" class="btn_min" @click="matchAddress(item)">선택</button>
                                  </template>
                                  <template v-if="(item.addressType === '00500'  && whereOpen === 'ShippingApiCreateReturn')">
                                    <button type="button" class="btn_min" @click="matchAddress(item)">선택</button>
                                  </template> -->
                                </td>
                            </tr>
                          </template>
                          </template>
                          <tr v-else>
                            <td colspan="6" class="emptyTable">
                              해당하는 내용이 없습니다.
                            </td>
                          </tr>
                        </tbody>
                    </table>
                </div>
            </div>
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
    <v-dialog :clickToClose=false />
  </div>
</template>

<script>
// import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '@/main'
// import http from '@/http-commons'

export default {
  name: 'ModalBundleList',
  data () {
    return {
      isLoading: false
    }
  },
  mounted () {
  },
  methods: {
    matchAddress (item) {
      if (this.whereOpen === 'ShippingApiCreateReturn') {
        eventBus.$emit('modalAddressListReturn', item)
      } else if (this.whereOpen === 'ShippingApiCreateRelease') {
        eventBus.$emit('modalAddressListRelease', item)
      }
      this.$emit('close')
    }
  },
  props: {
    'list': Array,
    'whereOpen': '', // 어디서 팝업 열었는지, 컴포넌트 명
    'userMenuAuth': Object
  }
}
</script>
