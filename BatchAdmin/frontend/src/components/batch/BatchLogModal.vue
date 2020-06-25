<template>
  <div name="modal-alert">
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="content employ">
          <div class="tableWrap tableList tableHover">
            <table class="textCenter" summary="배치리스트">
              <caption class="blind">배치리스트</caption>
              <colgroup>
                <col style="width:25%" />
                <col style="width:25%" />
                <col style="width:10%" />
                <col style="width:40%" />
                <col />
              </colgroup>
              <thead>
                <tr>
                  <th>시작시간</th>
                  <th>종료시간</th>
                  <th>결과</th>
                  <th>실패사유</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, idx) in batchLogList" :key="idx">
                  <td>{{ item.batch_start_date_desc }}</td>
                  <td>{{ item.batch_end_date_desc }}</td>
                  <td>{{ item.batch_result_type_code }}</td>
                  <td>{{ item.batch_fail_text }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="close()">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
import http from '../../http-commons'
export default {
  data () {
    return {
      batchLogList: []
    }
  },
  mounted () {
    this.getbatchLogList()
  },
  methods: {
    close () {
      this.$emit('close')
    },
    getbatchLogList () {
      http
        .get('/batchManage/batchLogList', {
          params: {
            batchNo: this.batchNo
          }
        })
        .then(result => {
          this.batchLogList = result.data
        })
        .catch(e => {
          console.log(e)
        })
    }
  },
  props: {
    'batchNo': String
  }
}
</script>
