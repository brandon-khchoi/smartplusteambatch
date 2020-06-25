<template>
  <div class="MenuAuth">
    <div class="content employ setting">
      <h1>권한 관리</h1>
      <div class="formContainer">
        <form action="#" method="post">
          <article>
            <div class="articleWrap group">
              <h5>권한 그룹 목록</h5>
              <ul class="groupList">
                <template v-for="(item, idx) in lunaAuthGroupNoData">
                  <li v-if="item.luna_auth_no != ''"
                    :key="idx"
                    @click="selectGroup(item.luna_auth_no)"
                    :class="{ selected: selectAuthNo == item.luna_auth_no }"
                  >
                    <span>
                      <input disabled name="authNameP" type="text" style="width:220px; margin-left: 0px; background: transparent; border: 0;" :value="item.luna_auth_name" />
                      <input name="authNameI"
                            style="display:none; width:220px; margin-left: 0px;"
                            type="text"
                            :value="item.luna_auth_name"
                      >
                    </span>
                    <div class="groupActions" name="groupModifyOrDelete" style="display:inline; line-height:34px;">
                      <i v-show="userMenuAuth.modify_data"
                         class="updateBtn fas fa-pen"
                         value="수정"
                         @click="modifyGroup(item, idx)"/>
                      <i v-show="userMenuAuth.delete_data && item.default_yn == 0"
                         class="deleteBtn fas fa-trash"
                         value="삭제"
                         @click="deleteGroup(item, idx)"/>
                    </div>

                    <div class="groupActions" name="groupUpdateOrCancel" style="display:none; line-height:34px;">
                      <i class="applyBtn fas fa-check" value="저장"
                         @click="updateGroup(item, idx)"/>
                      <i class="cancelBtn fas fa-times" value="취소"
                         @click="cancelGroup(item, idx)"/>
                    </div>
                  </li>

                  <li v-else :key="idx" class="groupInputField">
                    <input type="text"
                           placeholder="권한 그룹명을 입력하세요."
                           v-model="newGroupName">
                    <div class="groupActions">
                      <i class="applyBtn fas fa-check" value="저장" @click="saveGroup()"/>
                      <i class="cancelBtn fas fa-times" value="취소" @click="saveGroupCancel()"/>
                    </div>
                  </li>
                </template>
              </ul>
              <ul>
                <li v-show="userMenuAuth.create_data"
                    class="groupCreateField btnWrap">
                  <input v-show="!createBtnStat"
                    class="btn_save auth_update_btn"
                    type="button"
                    value="생성"
                    @click="createGroup()"
                  >
                </li>
              </ul>
            </div>

            <div class="articleWrap settingSection">
              <div class="menu">
                <h5>메뉴별 권한</h5>
                <div class="tableWrap tableList">
                  <table
                    class="textCenter"
                    summary="메뉴별 권한"
                  >
                    <caption class="blind">메뉴별 권한</caption>
                    <colgroup>
                      <col style="width:15%">
                      <col span="7">
                    </colgroup>
                    <thead>
                    <tr>
                      <th>&nbsp;</th>
                      <th>
                        메뉴노출
                        <br />
                        <p class="inputWrap">
                        <input type="checkbox"
                               id="check_all_menu_display" class="chk_all_class_group"
                               @click="funcCheckAll('menu_display', 'check_all_menu_display')"/>
                        <label for="check_all_menu_display"></label>
                        </p>
                      </th>
                      <th >
                        목록조회
                        <br />
                        <p class="inputWrap">
                        <input type="checkbox"
                               id="check_all_retrieve_list" class="chk_all_class_group"
                               @click="funcCheckAll('retrieve_list', 'check_all_retrieve_list')"/>
                        <label for="check_all_retrieve_list"></label>
                        </p>
                      </th>
                      <th>
                        상세읽기
                        <br />
                        <p class="inputWrap">
                        <input type="checkbox"
                               id="check_all_retrieve_detail" class="chk_all_class_group"
                               @click="funcCheckAll('retrieve_detail', 'check_all_retrieve_detail')"/>
                        <label for="check_all_retrieve_detail"></label>
                        </p>
                      </th>
                      <th>
                        등록/생성
                        <br />
                        <p class="inputWrap">
                        <input type="checkbox"
                               id="check_all_create_data" class="chk_all_class_group"
                               @click="funcCheckAll('create_data', 'check_all_create_data')"/>
                        <label for="check_all_create_data"></label>
                        </p>
                      </th>
                      <th>
                        수정
                        <br />
                        <p class="inputWrap">
                        <input type="checkbox"
                               id="check_all_modify_data" class="chk_all_class_group"
                               @click="funcCheckAll('modify_data', 'check_all_modify_data')"/>
                        <label for="check_all_modify_data"></label>
                        </p>
                      </th>
                      <th>
                        삭제
                        <br />
                        <p class="inputWrap">
                        <input type="checkbox"
                               id="check_all_delete_data" class="chk_all_class_group"
                               @click="funcCheckAll('delete_data', 'check_all_delete_data')"/>
                        <label for="check_all_delete_data"></label>
                        </p>
                      </th>
                      <th>
                        엑셀다운
                        <br/>
                        <p class="inputWrap">
                        <input type="checkbox"
                               id="check_all_excl_down" class="chk_all_class_group"
                               @click="funcCheckAll('excl_down', 'check_all_excl_down')"/>
                        <label for="check_all_excl_down"></label>
                        </p>
                      </th>
                    </tr>
                    </thead>
                    <tbody>
                    <template v-for="(menuFunc, idx) in menuAuth">
                      <tr :key="idx">
                        <td v-if="menuFunc.menu_sub_name == null"><h3>{{menuFunc.menu_main_name}}</h3></td>
                        <td v-else>{{menuFunc.menu_sub_name}}</td>
                        <td>
                          <p class="inputWrap">
                            <input
                              type="checkbox" :true-value=1 :false-value=0
                              v-model="menuFunc.menu_display"
                              :id="menuFunc.menu_auth_no + 'menu_display'"
                              :value="menuFunc.menu_auth_no + '_1'"
                              :checked="menuFunc.menu_display==1"
                            >
                            <label :for="menuFunc.menu_auth_no + 'menu_display'"></label>
                          </p>
                        </td>
                        <td>
                          <p class="inputWrap">
                            <input
                              type="checkbox" :true-value=1 :false-value=0
                              v-model="menuFunc.retrieve_list"
                              :id="menuFunc.menu_auth_no + 'retrieve_list'"
                              :value="menuFunc.menu_auth_no + '_2'"
                              :checked="menuFunc.retrieve_list==1"
                            >
                            <label :for="menuFunc.menu_auth_no + 'retrieve_list'"></label>
                          </p>
                        </td>
                        <td>
                          <p class="inputWrap">
                            <input
                              type="checkbox" :true-value=1 :false-value=0
                              v-model="menuFunc.retrieve_detail"
                              :id="menuFunc.menu_auth_no + 'retrieve_detail'"
                              :value="menuFunc.menu_auth_no + '_4'"
                              :checked="menuFunc.retrieve_detail==1"
                            >
                            <label :for="menuFunc.menu_auth_no + 'retrieve_detail'"></label>
                          </p>
                        </td>
                        <td>
                          <p class="inputWrap">
                            <input
                              type="checkbox" :true-value=1 :false-value=0
                              v-model="menuFunc.create_data"
                              :id="menuFunc.menu_auth_no + 'create_data'"
                              :value="menuFunc.menu_auth_no + '_8'"
                              :checked="menuFunc.create_data==1"
                            >
                            <label :for="menuFunc.menu_auth_no + 'create_data'"></label>
                          </p>
                        </td>
                        <td>
                          <p class="inputWrap">
                            <input
                              type="checkbox" :true-value=1 :false-value=0
                              v-model="menuFunc.modify_data"
                              :id="menuFunc.menu_auth_no + 'modify_data'"
                              :value="menuFunc.menu_auth_no + '_16'"
                              :checked="menuFunc.modify_data==1"
                            >
                            <label :for="menuFunc.menu_auth_no + 'modify_data'"></label>
                          </p>
                        </td>
                        <td>
                          <p class="inputWrap">
                            <input
                              type="checkbox" :true-value=1 :false-value=0
                              v-model="menuFunc.delete_data"
                              :id="menuFunc.menu_auth_no + 'delete_data'"
                              :value="menuFunc.menu_auth_no + '_32'"
                              :checked="menuFunc.delete_data==1"
                            >
                            <label :for="menuFunc.menu_auth_no + 'delete_data'"></label>
                          </p>
                        </td>
                        <td>
                          <p class="inputWrap">
                            <input
                              type="checkbox" :true-value=1 :false-value=0
                              v-model="menuFunc.excl_down"
                              :id="menuFunc.menu_auth_no + 'excl_down'"
                              :value="menuFunc.menu_auth_no + '_64'"
                              :checked="menuFunc.excl_down==1"
                            >
                            <label :for="menuFunc.menu_auth_no + 'excl_down'"></label>
                          </p>
                        </td>
                      </tr>
                    </template>
                    </tbody>
                  </table>
                  <div class="btnWrap">
                    <input v-show="userMenuAuth.modify_data"
                      class="btn btn_save"
                      type="button"
                      value="적용"
                      @click="set()"
                    >
                  </div>
                </div>
              </div>

              <div class="move">
                <div class="tableWrap tableList tableHover">
                  <table
                    class="textCenter"
                    summary="계정 권한 설정"
                  >
                    <caption class="blind">계정 권한 설정</caption>
                    <colgroup>
                      <col style="width:6%"/>
                      <col style="width:8%"/>
                      <col/>
                      <col/>
                      <col/>
                      <col/>
                      <col/>
                      <col/>
                    </colgroup>
                    <thead>
                      <tr>
                        <th>
                          선택<br />
                          <p class="inputWrap">
                          <input type="checkbox"
                                id="user_all" class="chk_all_class_group"
                                @click="userChkAll('menu_display', 'check_all_menu_display')"/>
                          <label for="user_all"></label>
                          </p>
                        </th>
                        <th>사번</th>
                        <th>소속</th>
                        <th>팀</th>
                        <th>파트</th>
                        <th>직책</th>
                        <th>한글이름</th>
                        <th>영문이름</th>
                      </tr>
                    </thead>
                    <tbody>
                    <template v-for="(user, idx) in menuUsers">
                      <tr :key="idx">
                        <td>
                          <p class="inputWrap">
                          <input
                            :id="user.luna_no"
                            type="checkbox" :true-value=user.luna_no :false-value=0
                            name="group_user">
                          <label :for="user.luna_no"></label>
                          </p>
                        </td>
                        <td>{{user.luna_id}}</td>
                        <td>{{user.luna_dept_name}}</td>
                        <td>{{user.luna_team_name}}</td>
                        <td>{{user.luna_part_name}}</td>
                        <td>{{user.luna_duty_name}}</td>
                        <td>{{user.luna_kor_name}}</td>
                        <td>{{user.luna_eng_kor_nick_name}}</td>
                      </tr>
                    </template>
                    </tbody>
                  </table>
                  <div v-show="userMenuAuth.modify_data"
                       class="groupMoveField btnWrap">
                    <select v-model="selectMoveAuthNo">
                      <option value>선택하세요</option>
                      <template v-for="(group, idx) in lunaAuthGroupNoData">
                        <option :key="idx" :value="group.luna_auth_no">{{group.luna_auth_name}}</option>
                      </template>
                    </select>
                    <input
                      class="btn_save"
                      type="button"
                      value="이동"
                      @click="groupMove()"
                    >
                  </div>
                </div>
              </div>
            </div>
          </article>
        </form>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import ModalAlert from '../common/modal/ModalAlert.vue'
import ModalDeleteConfirm from './modal/ModalDeleteConfirm.vue'
import http from '../../http-commons'
import { eventBus } from '../../main'

export default {
  mounted () {
    eventBus.$on('menuAuthModalCall', () => this.retreiveCommonData())
    this.retreiveCommonData()
  },
  data () {
    return {
      isLoading: false,

      createBtnStat: false,
      newGroupName: '',
      menuUsers: [],
      menuAuth: [],
      selectMoveAuthNo: '',
      selectAuthNo: 1,
      lunaAuthGroupNoData: []
    }
  },
  methods: {
    retreiveCommonData () {
      if (this.userMenuAuth.retrieve_list) {
        http
          .get('/account/authgrouplist')
          .then(result => {
            this.lunaAuthGroupNoData = result.data.auth_group_list
            this.selectGroup(this.selectAuthNo)
          })
          .catch(e => {
            console.log('MenuAuth.vue : ' + e)
          })
      }
    },
    selectGroup (authNo) {
      this.selectAuthNo = authNo
      this.get(authNo)
    },
    get (authNo) {
      this.isLoading = true
      http
        .get('/account/menuauthlist', {
          params: {
            auth_no: authNo
          }
        })
        .then(result => {
          this.isLoading = false
          this.menuUsers = result.data.menu_user_list
          this.menuAuth = result.data.menu_auth_list
        })
        .catch(e => {
          console.log('MenuAuth.vue : ' + e)
        })
    },
    funcCheckAll (fun, id) {
      let chk = document.getElementById(id).checked
      for (let i in this.menuAuth) {
        if (chk) {
          this.menuAuth[i][fun] = 1
        } else {
          this.menuAuth[i][fun] = 0
        }
      }
    },
    userChkAll () {
      let chk = document.getElementById('user_all').checked
      let size = this.menuUsers.length
      for (let i = 0; i < size; i++) {
        document.getElementsByName('group_user')[i].checked = chk
      }
    },
    set () {
      http
        .post('/account/menuauthmod', {
          params: {
            menu_auth: this.menuAuth,
            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(result => {
          var msg = ''
          if (result.data === '0000') {
            msg = '메뉴별 권한이 변경 되었습니다.'
          } else {
            msg = '메뉴별 권한변경을 실패했습니다.'
          }
          this.$modal.show(
            ModalAlert,
            {
              title: '메뉴별 권한',
              text: msg
            },
            {
              width: 450,
              height: 167
            }
          )
          if (result.data === '0000') {
            this.selectGroup(this.selectAuthNo)
            this.refreshDefault()
          }
        })
        .catch(e => {
          console.log('MenuAuth.vue : ' + e)
        })
    },
    groupMove () {
      let selectGroupCheck = this.selectMoveAuthNo
      if (selectGroupCheck === '') {
        this.$modal.show(
          ModalAlert,
          {
            title: '사용자 그룹이동',
            text: '변경대상 그룹을 선택해 주세요.'
          },
          {
            width: 450,
            height: 167
          }
        )
        return false
      }

      let size = this.menuUsers.length
      let arr = []
      for (let i = 0; i < size; i++) {
        let chk = document.getElementsByName('group_user')[i].checked
        if (chk) {
          arr.push(document.getElementsByName('group_user')[i].getAttribute('true-value'))
        }
      }

      if (arr.length > 0) {
        http
          .post('/account/userauthmod', {
            params: {
              move_auth_no: this.selectMoveAuthNo,
              move_user_list: arr,
              login_luna_no: this.$store.getters.getUser.luna_no
            }
          })
          .then(result => {
            let msg = ''
            if (result.data === '0000') {
              msg = '사용자 그룹이 변경 되었습니다.'
            } else {
              msg = '그룹 변경을 실패했습니다.'
            }
            this.$modal.show(
              ModalAlert,
              {
                title: '사용자 그룹이동',
                text: msg
              },
              {
                width: 450,
                height: 167
              }
            )

            if (result.data === '0000') {
              this.selectGroup(this.selectMoveAuthNo)
              this.refreshDefault()
            }
          })
          .catch(e => {
            console.log('MenuAuth.vue : ' + e)
          })
      }
    },

    createGroup () {
      if (this.createBtnStat) {
        this.createBtnStat = false
      } else {
        this.createBtnStat = true
        // this.$set(this.lunaAuthGroupNoData, '', '')
        this.lunaAuthGroupNoData.push({luna_auth_no: '', luna_auth_name: '', default_yn: 0})
      }
    },
    saveGroup () {
      http
        .post('/account/authgroupadd', {
          params: {
            new_group_name: this.newGroupName,
            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(result => {
          let msg = ''
          if (result.data === '0000') {
            msg = '권한 그룹이 등록되었습니다.'
          } else {
            msg = '권한 그룹 등록에 실패했습니다.'
          }
          this.$modal.show(
            ModalAlert,
            {
              title: '권한 그룹 등록',
              text: msg
            },
            {
              width: 450,
              height: 167
            }
          )

          if (result.data === '0000') {
            this.newGroupName = ''
            this.createBtnStat = false
            this.retreiveCommonData()
            this.refreshDefault()
          }
        })
        .catch(e => {
          console.log('MenuAuth.vue : ' + e)
        })
    },
    saveGroupCancel () {
      // this.$delete(this.lunaAuthGroupNoData, '')
      this.lunaAuthGroupNoData.pop()
      this.createBtnStat = false
    },

    modifyGroup (item, idx) {
      document.getElementsByName('authNameP')[idx].setAttribute('style', 'display:none; width:220px; background: transparent; border: 0;')
      document.getElementsByName('authNameI')[idx].setAttribute('style', 'display:inline; width:220px;')

      document.getElementsByName('groupModifyOrDelete')[idx].setAttribute('style', 'display:none; line-height:34px;')
      document.getElementsByName('groupUpdateOrCancel')[idx].setAttribute('style', 'display:inline; line-height:34px;')
    },
    deleteGroup (item, idx) {
      this.$modal.show(
        ModalDeleteConfirm,
        {
          title: '권한 그룹 삭제',
          data: item
        },
        {
          width: 450,
          height: 167
        }
      )
    },
    updateGroup (item, idx) {
      // eslint-disable-next-line camelcase
      let group_name = document.getElementsByName('authNameI')[idx].value
      // eslint-disable-next-line camelcase
      if (group_name !== '') {
        http
          .post('/account/authgroupupdate', {
            params: {
              luna_auth_no: item.luna_auth_no,
              mod_group_name: group_name,
              login_luna_no: this.$store.getters.getUser.luna_no
            }
          })
          .then(result => {
            let msg = ''
            if (result.data === '0000') {
              msg = '권한 그룹명이 변경되었습니다.'
            } else {
              msg = '권한 그룹명 변경에 실패했습니다.'
            }
            this.$modal.show(
              ModalAlert,
              {
                title: '권한 그룹명 변경',
                text: msg
              },
              {
                width: 450,
                height: 167
              }
            )

            if (result.data === '0000') {
              document.getElementsByName('authNameP')[idx].setAttribute('style', 'display:inline; width:220px; background: transparent; border: 0;')
              document.getElementsByName('authNameI')[idx].setAttribute('style', 'display:none; width:220px;')

              document.getElementsByName('groupModifyOrDelete')[idx].setAttribute('style', 'display:inline; line-height:34px;')
              document.getElementsByName('groupUpdateOrCancel')[idx].setAttribute('style', 'display:none; line-height:34px;')
              this.retreiveCommonData()
              this.refreshDefault()
            }
          })
          .catch(e => {
            console.log('MenuAuth.vue : ' + e)
          })
      }
    },
    cancelGroup (item, idx) {
      document.getElementsByName('authNameP')[idx].setAttribute('style', 'display:inline; width:220px; background: transparent; border: 0;')
      document.getElementsByName('authNameI')[idx].setAttribute('style', 'display:none; width:220px;')

      document.getElementsByName('groupModifyOrDelete')[idx].setAttribute('style', 'display:inline; line-height:34px;')
      document.getElementsByName('groupUpdateOrCancel')[idx].setAttribute('style', 'display:none; line-height:34px;')
    },
    /**
     * 설정 변경 후 선택값 초기화
     */
    refreshDefault () {
      // 선택 유저 초기화
      let size = this.menuUsers.length
      for (let i = 0; i < size; i++) {
        document.getElementsByName('group_user')[i].checked = false
      }
      // 선택 권한그룹 초기화
      this.selectMoveAuthNo = ''
      // 전체선택 체크박스 초기화
      let chkAllGroup = document.getElementsByClassName('chk_all_class_group')
      let chkAllSize = chkAllGroup.length
      for (let i = 0; i < chkAllSize; i++) {
        chkAllGroup[i].checked = false
      }
    }
  },
  props: [
    'userMenuAuth'
  ]
}
</script>
