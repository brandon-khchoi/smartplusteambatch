<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          묶음배송비 계산 방식

        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="popupContentWrap">
            <div class="popupContent">
                <div class="tableWrap tableList tableHover" >
                    <table>
                        <colgroup>
                          <col style="width:15%"/>
                          <col style="width:15%"/>
                          <col style="width:9%"/>
                          <col style="width:9%"/>
                          <col style="width:12%"/>
                          <col style="width:12%"/>
                          <col style="width:8%"/>
                          <col style="width:10%"/>
                        </colgroup>
                        <thead>
                          <th>그룹번호</th>
                          <th>그룹명</th>
                          <th>계산방식</th>
                          <th>권역구분</th>
                          <th>권역2 추가배송비</th>
                          <th>권역3 추가배송비</th>
                          <th>사용여부</th>
                          <th>선택</th>
                        </thead>
                        <tbody>
                          <template v-if="list.length > 0">
                          <template v-for="item in list">
                            <tr :key = "item.id">
                                <td class="text-center">
                                  {{item.code}}
                                </td>
                                <td class="text-center">
                                  {{item.name}}
                                </td>
                                <td class="text-center">
                                  <template v-if="item.value.bundleGroup && item.value.bundleGroup.calculationMethod">
                                    <template v-if="item.value.bundleGroup.calculationMethod === '1'">
                                      최소부과
                                    </template>
                                    <template v-if="item.value.bundleGroup.calculationMethod === '2'">
                                      최대부과
                                    </template>
                                  </template>
                                </td>
                                <td class="text-center">
                                  <template v-if="item.value.bundleGroup && item.value.bundleGroup.deliveryFeeByArea && item.value.bundleGroup.deliveryFeeByArea.areaType">
                                    {{item.value.bundleGroup.deliveryFeeByArea.areaType}}권역
                                  </template>
                                </td>
                                <td class="text-center">
                                   <template v-if="item.value.bundleGroup && item.value.bundleGroup.deliveryFeeByArea && item.value.bundleGroup.deliveryFeeByArea.area2ExtraFee">
                                    {{item.value.bundleGroup.deliveryFeeByArea.area2ExtraFee}}
                                  </template>

                                </td>
                                <td class="text-center">
                                  <template v-if="item.value.bundleGroup && item.value.bundleGroup.deliveryFeeByArea && item.value.bundleGroup.deliveryFeeByArea.area3ExtraFee">
                                    {{item.value.bundleGroup.deliveryFeeByArea.area3ExtraFee}}
                                  </template>
                                </td>
                                <td class="text-center">
                                  <template v-if="item.value.bundleGroup && item.value.bundleGroup.available">
                                    {{item.value.bundleGroup.available}}
                                  </template>
                                </td>
                                <td class="textCenter">
                                  <button type="button" class="btn_min" @click="matchBundle(item)">확인</button>
                                </td>
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
    console.log(this.list)
  },
  methods: {
    matchBundle (item) {
      eventBus.$emit('modalBundleList', item)
      this.$emit('close')
    }
  },
  props: {
    'list': Array,
    'userMenuAuth': Object
  }
}
</script>
