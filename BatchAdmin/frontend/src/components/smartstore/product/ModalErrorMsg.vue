<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="popupContentWrap">
            <div class="popupContent">
                 <p style="text-align:left;font-weight:500;">상품연동을 완료했습니다.</p>
                <template v-if="list.legth === 0">
                </template>
                <!-- :style="[item.changed ? { 'background': '#fff0f0'} : {}]" -->
                <div class="tableWrap tableList tableHover">
                    <table>
                        <colgroup>
                          <col style="width:20%"/>
                          <col style="width:40%"/>
                          <col style="width:40%"/>
                        </colgroup>
                        <thead>
                          <tr>
                          <th colspan="3">실패상품목록</th>
                          </tr>
                          <tr>
                          <th>상품ID</th>
                          <th>상품명</th>
                          <th>사유</th>
                          </tr>
                        </thead>
                        <tbody>
                          <template v-for="item in list">
                            <tr :key = "item.id">
                                <td class="text-center">{{item.product_no}}</td>
                                <td class="text-center">{{item.product_name}}</td>
                                <td class="text-center">
                                    {{item.errorMsg}}
                                </td>
                            </tr>
                          </template>
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
export default {
  name: 'ModalErrorMsg',
  data () {
    return {
      isLoading: false
    }
  },
  mounted () {
  },
  methods: {

  },
  props: {
    'list': Array
  }
}
</script>
