<template>
  <div name="modal-corp-search" class="ModalCorpSearch">
    <div class="vue-modal-box">
      <div class="dialog-content">
          <div class="titleWrap">
            <h1>리로드 서비스 사용 이력 보기</h1>
          </div>
          <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
          <div class="dialog-c-text">
              <div class="popupContentWrap">
                  <div class="popupContent">
                      <article style="overflow-y:scroll;height:500px">
                        <div class="articleWrap">
                          <div class="tableWrap tableList scrollWrap">
                            <table summary="리로드 서비스 사용 이력 보기">
                              <caption class="blind">리로드 서비스 사용 이력 보기</caption>
                              <colgroup>
                                <col width="20px"/>
                                <col width="50px"/>
                                <col width="60px"/>
                                <col width="60px"/>
                                <col width="40px"/>
                                <col width="150px"/>
                                <col width="60px"/>
                                <col width="60px"/>
                              </colgroup>
                              <thead>
                                <th>No</th>
                                <th>구분</th>
                                <th>시작일</th>
                                <th>종료일</th>
                                <th>일수</th>
                                <th>메모</th>
                                <th>작업자</th>
                                <th>작업일시</th>
                              </thead>
                              <tbody>
                                <template v-if="resultData.length != 0">
                                  <tr v-for="(item,idx) in resultData" :key="item.id" >
                                    <td class="text-center">{{idx+1}}</td>
                                    <td class="text-center">{{item.history_type_text}}</td>
                                    <td class="text-center">{{item.service_start_date}}</td>
                                    <td class="text-center">{{item.service_end_date}}</td>
                                    <td class="text-center" style="text-align:right;">{{item.service_extend_day}} 일</td>
                                    <td class="text-center" style="text-align:left;">{{item.memo_text}}</td>
                                    <td class="text-center">{{item.memo_user_name_kor}}({{item.memo_user_name_eng}})</td>
                                    <td class="text-center">{{item.history_date}}</td>
                                  </tr>
                                </template>
                                <tr v-else>
                                  <td colspan="8">
                                    서비스 사용 이력이 없습니다.
                                  </td>
                                </tr>
                              </tbody>
                            </table>
                          </div>
                        </div>
                      </article>
                  </div>
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
export default {
  data () {
    return {
      isLoading: false,
      resultData: {}
    }
  },
  created () {
    this.get()
  },
  methods: {
    get () {
      let memId = this.data.member_id
      let shopId = this.data.member_mall_no

      this.isLoading = true
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId + '/histories')
        .then(response => {
          this.resultData = response.data.data
          this.isLoading = false
        })
        .catch(e => {
          this.isLoading = false
          console.log(e)
        })
    }
  },
  props: {
    'data': Object,
    'reloadHttp': Object
  }
}
</script>
