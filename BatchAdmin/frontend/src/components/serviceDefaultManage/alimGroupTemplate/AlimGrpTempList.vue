<template>
  <div class="AlimGrpTempList">
    <div class="content">
      <h1>알림톡그룹템플릿 관리</h1>
      <div class="formContainer">
        <div class="tapContiner">

          <article class="searchResult">
            <div class="articleWrap">
              <div class="tableTopWrap">
                <p class="total">
                  결과 : 총
                  <span>{{total_cnt}}</span> 건
                </p>
                <div class="btnWrap">
                  <ul>
                    <li v-show="userMenuAuth.create_data">
                      <p class="btn_add auth_create">
                        <a @click="createNewTemplate()">
                          <i class="fas fa-plus"></i>등록
                        </a>
                      </p>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="tableWrap tableList tableHover">
                <table class="textCenter" summary="계정리스트">
                  <caption class="blind">알림톡그풉템플릿 목록</caption>
                  <colgroup>
                    <col style="width:10%">
                    <col style="width:60%">
                    <col style="width:15%">
                    <col style="width:15%">
                    <col>
                  </colgroup>
                  <thead>
                    <tr>
                      <th>허브파트너</th>
                      <th>파일명</th>
                      <th>등록자</th>
                      <th>등록일</th>
                    </tr>
                  </thead>
                  <tbody v-if="total_cnt > 0">
                    <tr v-for="(item, idx) in templateListData" :key="idx">

                      <td>{{item.hubPartner}}</td>
                      <td style="text-align:left;"><a :href="item.fileSaveName">{{item.fileUploadName}}</a></td>
                      <td style="text-align:left;">{{item.lunaUserName}}</td>
                      <td>{{item.createdDate}}</td>
                    </tr>
                  </tbody>
                  <tbody v-else>
                    <tr>
                      <td colspan="4">
                        등록된 파일이 없습니다.
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <Pagination
                  :paginate="templateListData.paginate"
                  :action="get"
                  :isPropPageCntShow=true>
              </Pagination>
            </div>
          </article>
        </div>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import Pagination from '@/components/common/Pagination'

export default {
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      templateListData: [],
      total_cnt: 0
    }
  },
  methods: {
    get (current_page = 1, per_page = 20) {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          // eslint-disable-next-line camelcase
          .get('/AlimTalk/GroupTemplate/log/' + current_page + '/' + per_page + '/10')
          .then(result => {
            this.isLoading = false
            this.templateListData = result.data.items
            this.total_cnt = result.data.paging.totalCount

            let page = {
              'per_page': result.data.paging.rowCount,
              'total_cnt': result.data.paging.totalCount,
              'last_page': result.data.paging.lastPage,
              'current_page': result.data.paging.currentPage
            }
            // eslint-disable-next-line no-undef
            this.templateListData.paginate = makePaginate(page)
            // eslint-disable-next-line no-undef
            this.templateListData.max = pageMaxNumber(page)
          })
          .catch(e => {
            this.isLoading = false
            console.log('AlimGrpTempList.vue : ' + e)
          })
      }
    },
    createNewTemplate () {
      this.$router.push({
        name: 'alimgrptempcreate'
      })
    }
  },
  components: {
    Pagination
  },
  props: [
    'userMenuAuth',
    'http'
  ]
}
</script>
