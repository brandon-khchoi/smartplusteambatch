<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          택배사 조회
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="popupContentWrap">
            <div class="popupContent">
                <div class="tableWrap tableList tableHover" >
                    <table>
                        <colgroup>
                          <col style="width:30%"/>
                          <col style="width:70%"/>
                        </colgroup>
                        <thead>
                          <th>택배사코드</th>
                          <th>택배사명</th>
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
                            </tr>
                          </template>
                          </template>
                          <tr v-else>
                            <td colspan="2" class="emptyTable">
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
// import { eventBus } from '@/main'
// import http from '@/http-commons'

export default {
  name: 'ModalReturnsCompanyList',
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
    'list': Array,
    'userMenuAuth': Object
  }
}
</script>
