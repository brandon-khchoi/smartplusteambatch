<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          {{ title }}
        </div>
        <div class="dialog-c-text">
          <div class="tableWrap tableList scrollWrap">
              <table>
                  <colgroup>
                      <col width="40%"/>
                      <col width="60%"/>
                  </colgroup>
                  <thead>
                    <th>배치명</th>
                    <th>실패사유</th>
                  </thead>
                  <tbody>
                    <template v-for="item in list">
                      <tr :key = "item.id">
                          <td class="text-center">{{item.batchName}}</td>
                          <td class="text-center">{{item.errorText}}</td>
                      </tr>
                    </template>
                    <!-- <tr v-else>
                      <td colspan="5" class="emptyTable">
                        해당하는 내용이 없습니다.
                      </td>
                    </tr> -->
                  </tbody>
              </table>
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ModalTable',
  data () {
    return {
    }
  },
  props: {
    'text': String,
    'title': String,
    'list': Array
  }
}
</script>
