<template>
  <div class="AccountList">
    <div class="content employ">
      <h1>계정 관리</h1>
      <div class="formContainer">
        <div class="tapContiner">
          <div class="searchWrap">
            <table summary="계정 조회">
              <caption class="blind">계정조회</caption>
              <colgroup>
                <col style="width:100px">
                <col style="width:calc(25% - 100px)">
                <col style="width:100px">
                <col style="width:calc(25% - 100px)">
                <col style="width:100px">
                <col style="width:calc(25% - 100px)">
                <col style="width:100px">
              </colgroup>
              <tbody>
                <tr>
                  <th>검색구분</th>
                  <td colspan="7">
                    <p class="searchWordWrap short7">
                      <select v-model="searchFlag">
                        <option value="1">한글이름</option>
                        <option value="2">사번</option>
                        <option value="3">영문이름(한글)</option>
                      </select>
                      <input type="text" v-model="searchTxt">
                    </p>
                  </td>
                </tr>
                <tr>
                  <th>소속</th>
                  <td>
                    <select v-model="deptNo">
                      <option value>전체</option>
                      <template v-for="(val,idx) in lunaDeptNoData">
                        <option :value="idx" :key="idx">{{val}}</option>
                      </template>
                    </select>
                  </td>
                  <th>팀</th>
                  <td>
                    <select v-model="teamNo">
                      <option value>전체</option>
                      <template v-for="(val,idx) in selectTeamData">
                        <option :value="idx" :key="idx">{{val}}</option>
                      </template>
                    </select>
                  </td>
                  <th>권한그룹</th>
                  <td>
                    <select v-model="authGroup">
                      <option value>전체</option>
                      <template v-for="(val,idx) in lunaAuthGroupNoData">
                        <option :value="idx" :key="idx">{{val}}</option>
                      </template>
                    </select>
                  </td>
                  <td colspan="2">
                    <div class="idWrap">
                      <p class="inputWrap">
                        <input type="checkbox" id="leave" v-model="resignYn" :true-value="1" :false-value="0">
                        <label for="leave">퇴사</label>
                      </p>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="btnWrap">
              <ul>
                <li v-show="userMenuAuth.retrieve_list">
                  <p class="btn_search" @click="get()">
                    <i class="fas fa-search"></i>검색
                  </p>
                </li>
              </ul>
            </div>
          </div>

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
                        <a @click="createNewAccount()">
                          <i class="fas fa-plus"></i>계정 생성
                        </a>
                      </p>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="tableWrap tableList tableHover">
                <table class="textCenter" summary="계정리스트">
                  <caption class="blind">계정리스트</caption>
                  <colgroup>
                    <col style="width:11%">
                    <col style="width:20%">
                    <col style="width:12%">
                    <col style="width:12%">
                    <col style="width:12%">
                    <col style="width:14%">
                    <col style="width:20%">
                    <col style="width:20%">
                    <col style="width:20%">
                    <col style="width:20%">
                    <col>
                  </colgroup>
                  <thead>
                    <tr>
                      <th>사번</th>
                      <th>소속</th>
                      <th>팀</th>
                      <th>파트</th>
                      <th>직책</th>
                      <th>한글이름</th>
                      <th>영문이름</th>
                      <th>휴대폰번호</th>
                      <th>이메일주소</th>
                      <th>권한그룹</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, idx) in account_list.items" :key="idx">

                      <td>
                        <a  v-if="userMenuAuth.retrieve_detail"
                            class="spanHover"
                            @click="editAccount(item.luna_no)">
                          <strong>{{item.luna_id}}</strong>
                        </a>
                        <span v-else>{{item.luna_id}}</span>
                      </td>
                      <td>{{item.luna_dept_name}}</td>
                      <td>{{item.luna_team_name}}</td>
                      <td>{{item.luna_part_name}}</td>
                      <td>{{item.luna_duty_name}}</td>
                      <td>{{item.luna_kor_name}}</td>
                      <td>{{item.luna_eng_kor_nick_name}}</td>
                      <td>{{item.luna_mobile_no}}</td>
                      <td>{{item.luna_email_addr}}</td>
                      <td>{{item.luna_auth_name}}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <Pagination
                  :paginate="account_list.paginate"
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
import http from '../../http-commons'

export default {
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      account_list: [],
      total_cnt: 0,

      searchFlag: 1,
      searchTxt: '',
      deptNo: '',
      teamNo: '',
      selectTeamData: null,
      authGroup: '',
      resignYn: false
    }
  },
  watch: {
    deptNo () {
      let teamDpCode = ''
      this.selectTeamData = {}
      this.teamNo = ''
      for (let idx in this.lunaTeamNoData) {
        teamDpCode = this.lunaTeamNoData[idx].luna_dept_no
        if (teamDpCode === this.deptNo) {
          this.selectTeamData[this.lunaTeamNoData[idx].luna_team_no] = this.lunaTeamNoData[idx].luna_team_name
        }
      }
    }
  },
  methods: {
    get (current_page = 1, per_page = 20) {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        http
          .get('/account/accountlist', {
            params: {
              current_page: current_page,
              per_page: per_page,

              search_flag: this.searchFlag,
              search_txt: this.searchTxt,
              dept_no: this.deptNo,
              team_no: this.teamNo,
              auth_group: this.authGroup,
              resign_yn: this.resignYn
            }
          })
          .then(result => {
            this.isLoading = false
            this.account_list = result.data
            this.total_cnt = result.data.pagination.total_cnt
            // eslint-disable-next-line no-undef
            this.account_list.paginate = makePaginate(result.data.pagination)
            // eslint-disable-next-line no-undef
            this.account_list.max = pageMaxNumber(result.data.pagination)
          })
          .catch(e => {
            console.log('AccountList.vue : ' + e)
          })
      }
    },
    createNewAccount () {
      this.$router.push({
        name: 'accountcreate'
      })
    },
    // eslint-disable-next-line camelcase
    editAccount (luna_no) {
      this.$router.push({
        name: 'accountedit',
        params: { luna_no: luna_no }
      })
    }
  },
  components: {
    Pagination
  },
  props: [
    'userMenuAuth',
    'lunaDeptNoData',
    'lunaTeamNoData',
    'lunaPartNoData',
    'lunaDutyNoData',
    'lunaAuthGroupNoData'
  ]
}
</script>
