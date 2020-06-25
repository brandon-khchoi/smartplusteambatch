<template>
  <div class="MenuConf">
    <div class="content employ setting">
      <h1>메뉴 관리</h1>
      <div class="formContainer">
        <form action="#" method="post">
          <article>
            <!-- new: {{menuConfDetailNew}}<br/>
            org: {{menuConfDetailOrg}}<br/>
            dft: {{menuConfDetail}}<br/> -->
            <div class="articleWrap group menuDirectoryField">
              <Ztree
                :nodes="menuConfList"
                :selectId="selectedNodeId"
                @onClick="selected"
              />

              <div class="menuCreateWrap btnWrap">
                <input v-show="userMenuAuth.create_data && !menuDetailCreateStat"
                      type="button"
                      class="btn_save"
                      value="생성"
                      @click="createMenu()">
              </div>
            </div>
            <div class="settingSection">
              <div class="articleWrap">
                <div class="titleWrap">
                  <p class="title">메뉴 정보</p>
                </div>
                <div class="detailContent">
                <!--start : 조회 및 수정 div-->
                  <div v-show="!menuDetailCreateStat" class="tableWrap tableRegister">
                    <table summary="메뉴 정보">
                      <caption class="blind"> 메뉴 정보</caption>
                      <colgroup>
                        <col style="width:160px">
                        <col>
                      </colgroup>
                      <tbody>
                        <tr>
                          <th>메뉴코드</th>
                          <td>
                            <div class="idWrap">
                            <input type="text" v-model="menuConfDetail.menu_no" disabled="disabled">
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <th>메뉴명 *</th>
                          <td>
                            <input type="text" maxlength="8" minlength="2"
                                  @input="typing($event, 'krnb', 'menu_name')"
                                  v-model="menuConfDetail.menu_name"
                                  :disabled="!menuDetailModStat">
                          </td>
                        </tr>
                        <tr>
                          <th>URL *</th>
                          <td>
                            <input type="text" maxlength="200" :disabled="!menuDetailModStat"
                                  v-model="menuConfDetail.menu_url_addr">
                          </td>
                        </tr>
                        <tr>
                          <th>메뉴단계 *</th>
                          <td>
                            <select v-model="menuConfDetail.menu_level" disabled>
                              <template v-for="(val,idx) in menuStep">
                                <option :value="val.menu_level" :key="idx">
                                  {{val.menu_level_name}}
                                </option>
                              </template>
                            </select>
                          </td>
                        </tr>
                        <tr>
                          <th>상위메뉴 *</th>
                          <td>
                            <select v-model="menuConfDetail.parent_menu_no" :disabled="!menuDetailModStat">
                              <option v-if="menuConfDetail.menu_level == 'group'" value="">상위메뉴없음</option>
                              <template v-for="(val,idx) in menuGroup">
                                <option v-if="menuConfDetail.menu_level == 'main'" :value="val.menu_group_no" :key="idx">
                                  {{val.menu_group_name}}
                                </option>
                              </template>
                              <template v-for="(val,idx) in parentMenu">
                                <option v-if="menuConfDetail.menu_level == 'sub'" :value="val.menu_main_no" :key="idx">
                                  {{val.menu_main_name}}
                                </option>
                              </template>
                            </select>
                          </td>
                        </tr>
                        <tr>
                          <th>사용여부 *</th>
                          <td>
                            <input id="use_yn" type="checkbox"
                                  v-model="menuConfDetail.use_yn"
                                  :true-value=1 :false-value=0
                                  :disabled="!menuDetailModStat">
                            <label for="use_yn">사용</label>
                          </td>
                        </tr>
                        <tr v-show="!menuDetailModStat">
                          <td colspan="2" class="btnWrap textRight">
                            <input v-show="userMenuAuth.delete_data" type="button" @click="delteMenuInfo()" class="btn_cancel" value="삭제">
                            <input v-show="userMenuAuth.modify_data" type="button" @click="modifyMenuInfo()" class="btn_save" value="수정">
                          </td>
                        </tr>
                        <tr v-show="menuDetailModStat">
                          <td colspan="2" class="btnWrap textRight">
                            <input type="button" @click="modifyMenuInfoCancel()" class="btn_cancel" value="취소">
                            <input type="button" @click="modifyMenuInfoSave()" class="btn_save" value="저장">
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <!--end : 조회 및 수정 div-->
                  <!--start : 신규생성 div-->
                  <div v-show="menuDetailCreateStat" class="tableWrap tableRegister">
                    <table summary="메뉴 정보">
                      <caption class="blind"> 메뉴 정보</caption>
                      <colgroup>
                        <col style="width:160px">
                        <col>
                      </colgroup>
                      <tbody>
                        <tr>
                          <th>메뉴코드</th>
                          <td>
                            <div class="idWrap">
                              <input type="text" v-model="menuConfDetailNew.menu_no" disabled="disabled">
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <th>메뉴명 *</th>
                          <td>
                            <input type="text" maxlength="8" minlength="2"
                                  v-model="menuConfDetailNew.menu_name"
                                  @input="typing($event, 'krnb', 'new_menu_name')">
                          </td>
                        </tr>
                        <tr>
                          <th>URL *</th>
                          <td>
                            <input type="text" maxlength="200" minlength="2"
                                  v-model="menuConfDetailNew.menu_url_addr">
                          </td>
                        </tr>
                        <tr>
                          <th>메뉴단계 *</th>
                          <td>
                            <select v-model="menuConfDetailNew.menu_level">
                              <template v-for="(val,idx) in menuStep">
                                <option :value="val.menu_level" :key="idx">
                                  {{val.menu_level_name}}
                                </option>
                              </template>
                            </select>
                          </td>
                        </tr>
                        <tr>
                          <th>상위메뉴 *</th>
                          <td>
                            <select v-model="menuConfDetailNew.parent_menu_no">
                              <option v-if="menuConfDetailNew.menu_level == 'group'" value="">상위메뉴없음</option>
                              <template v-for="(val,idx) in menuGroup">
                                <option v-if="menuConfDetailNew.menu_level == 'main'" :value="val.menu_group_no" :key="idx">
                                  {{val.menu_group_name}}
                                </option>
                              </template>
                              <template v-for="(val,idx) in parentMenu">
                                <option v-if="menuConfDetailNew.menu_level == 'sub'" :value="val.menu_main_no" :key="idx">
                                  {{val.menu_main_name}}
                                </option>
                              </template>
                            </select>

                          </td>
                        </tr>
                        <tr>
                          <th>사용여부 *</th>
                          <td>
                            <input id="use_yn" type="checkbox"
                                  v-model="menuConfDetailNew.use_yn"
                                  :true-value=1 :false-value=0>
                            <label for="use_yn">사용</label>
                          </td>
                        </tr>
                        <tr>
                          <td colspan="2" class="btnWrap textRight">
                            <input type="button" @click="createMenuInfoCancel()" class="btn_cancel" value="취소">
                            <input type="button" @click="createMenuInfoSave()" class="btn_save" value="저장">
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <!--end : 신규생성 div-->
                </div>
              </div>

              <div class="articleWrap">
                <div class="titleWrap">
                  <p class="title">하위메뉴 순서 변경</p>
                </div>
                <div class="menuSortField detailContent">
                  <draggable v-show="userMenuAuth.modify_data"
                             v-model="menuConfDetailList"
                            @change="changSort($event, menuConfDetailList)"
                            @start="drag=true"
                            @end="drag=false">
                    <ul class="menuSortable" v-for="(item, idx) in menuConfDetailList" :key="idx">
                      <li>
                        <span>{{item.menu_name}}</span>
                        <i class="sortableIcon fas fa-bars"></i>
                      </li>
                    </ul>
                  </draggable>
                  <ul v-show="!userMenuAuth.modify_data"
                      class="menuSortable" v-for="(item, idx) in menuConfDetailList" :key="idx">
                      <li>
                        <span>{{item.menu_name}}</span>
                        <i class="sortableIcon fas fa-bars"></i>
                      </li>
                    </ul>
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
import http from '../../http-commons'
import draggable from 'vuedraggable'
import ModalConfirm from '../common/modal/ModalConfirm.vue'
import ModalAlert from '../common/modal/ModalAlert'
import { eventBus } from '../../main'
import Ztree from './Ztree'

export default {
  mounted () {
    eventBus.$on('delteMenuInfo', () => this.retreiveCommonData())
    eventBus.$on('modifyMenuInfoCancel', () => this.menuDetailModStatModCancel())
    eventBus.$on('modifyMenuInfoSave', () => this.menuDetailModStatModSave())
    eventBus.$on('createMenuInfoCancel', () => this.menuDetailCreateCancel())
    eventBus.$on('createMenuInfoSave', () => this.menuDetailCreateSave())
    this.retreiveCommonData()
      .then(result => {
        const rootObj = this.menuConfList.filter(menu => {
          return menu.id === 'group001'
        })
        this.selected(null, null, rootObj[0])
      })
  },
  beforeDestroy () {
    eventBus.$off('delteMenuInfo')
    eventBus.$off('modifyMenuInfoCancel')
    eventBus.$off('modifyMenuInfoSave')
    eventBus.$off('createMenuInfoCancel')
    eventBus.$off('createMenuInfoSave')
  },
  data () {
    return {
      isLoading: false,
      selectedNodeId: '',
      menuConfList: [],
      menuStep: [
        {menu_level: 'sub', menu_level_name: '상세메뉴'},
        {menu_level: 'main', menu_level_name: '메인메뉴'},
        {menu_level: 'group', menu_level_name: '그룹메뉴'}
      ],
      menuGroup: [],
      parentMenu: [],
      menuConfDetail: [],
      menuConfDetailOrg: [],
      menuConfDetailNew: [],
      menuConfDetailList: [],
      menuDetailModStat: false,
      menuDetailCreateStat: false,

      valiAlertMain: {
        menu_name: '메뉴명을 입력해주세요.',
        menu_level: '메뉴단계를 선택해주세요.',
        parent_menu_no: '상위메뉴를 선택해주세요.'
      },
      valiAlertSub: {
        menu_name: '메뉴명을 입력해주세요.',
        menu_url_addr: 'URL을 입력해주세요.',
        menu_level: '메뉴단계를 선택해주세요.',
        parent_menu_no: '상위메뉴를 선택해주세요.'
      }
    }
  },
  methods: {
    retreiveCommonData () {
      if (this.userMenuAuth.retrieve_list) {
        return http
          .get('/menuconf/menuconflist', {
            params: {}
          })
          .then(result => {
            this.menuConfList = result.data.menu_conf_list
            this.menuGroup = result.data.menu_group
            this.parentMenu = result.data.parent_menu
          })
          .catch(e => {
            console.log('MenuConf.vue : ' + e)
          })
      }
    },
    menuDetailModStatModCancel () {
      this.menuDetailModStat = !this.menuDetailModStat
      this.menuConfDetail = this.menuConfDetailOrg
      this.retreiveCommonData()
    },
    menuDetailModStatModSave () {
      this.menuDetailModStat = !this.menuDetailModStat
      this.retreiveCommonData()
    },
    menuDetailCreateCancel () {
      this.menuDetailCreateStat = !this.menuDetailCreateStat
      this.retreiveCommonData()
    },
    menuDetailCreateSave () {
      this.menuDetailCreateStat = !this.menuDetailCreateStat
      this.retreiveCommonData()
    },
    // receive selected tree node info
    selected (_, __, nodes) {
      this.isLoading = true
      this.menuDetailModStat = false
      this.menuDetailCreateStat = false

      this.selectedNodeId = nodes.id

      http
        .get('/menuconf/menudetail', {
          params: {
            'menu_no': nodes.menu_no,
            'menu_level': nodes.menu_level
          }
        })
        .then(result => {
          this.isLoading = false
          this.menuConfDetail = result.data.menu_detail
          this.menuConfDetailList = result.data.menu_detail_list
        })
        .catch(e => {
          console.log('MenuConf.vue : ' + e)
        })
    },
    changSort (evt, data) {
      http
        .post('/menuconf/menusortchange', {
          params: {
            menu_sort_list: this.menuConfDetailList,
            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(result => {
          if (result.data === '0000') {
            this.retreiveCommonData()
          }
        })
        .catch(e => {
          console.log('MenuConf.vue : ' + e)
        })
    },
    delteMenuInfo () {
      /* 하위메뉴가 있으면 삭제불가 */
      let deleteAble = true
      let delMenuLevel = this.menuConfDetail.menu_level
      let delMenuNo = this.menuConfDetail.menu_no

      this.menuConfList.forEach(menu => {
        if (delMenuLevel === 'group') {
          if (menu.menu_level === 'main' && Number(menu.parent_menu_no) === delMenuNo) {
            deleteAble = false
          }
        } else if (delMenuLevel === 'main') {
          if (menu.menu_level === 'sub' && Number(menu.parent_menu_no) === delMenuNo) {
            deleteAble = false
          }
        }
      })

      if (deleteAble) {
        this.$modal.show(
          ModalConfirm,
          {
            title: '메뉴 삭제',
            data: {
              url: '/menuconf/menudel',
              item: {
                menu_level: this.menuConfDetail.menu_level,
                menu_no: this.menuConfDetail.menu_no,
                login_luna_no: this.$store.getters.getUser.luna_no
              },
              refresh: 'delteMenuInfo'
            }
          },
          {
            width: 450,
            height: 167
          }
        )
      } else {
        this.$modal.show(
          ModalAlert,
          {
            text: '하위메뉴가 있어 삭제가 불가합니다.',
            title: '메뉴 삭제'
          },
          {
            width: 450,
            height: 167
          }
        )
      }
    },
    modifyMenuInfo () {
      this.menuConfDetailOrg = Object.assign({}, this.menuConfDetail)
      this.menuDetailModStat = true
    },
    modifyMenuInfoCancel () {
      this.$modal.show(
        ModalConfirm,
        {
          title: '메뉴 정보 수정 취소',
          data: {
            url: '',
            item: {},
            refresh: 'modifyMenuInfoCancel'
          }
        },
        {
          width: 450,
          height: 167
        }
      )
    },
    modifyMenuInfoSave () {
      /* 빈값 및 기타 valid check */
      if (!this.validation(this.menuConfDetail, '메뉴정보 수정')) return false
      this.$modal.show(
        ModalConfirm,
        {
          title: '메뉴 정보 수정 저장',
          data: {
            url: '/menuconf/menudetailmod',
            item: {
              menu_level: this.menuConfDetail.menu_level,
              menu_no: this.menuConfDetail.menu_no,
              menu_name: this.menuConfDetail.menu_name,
              menu_url_addr: this.menuConfDetail.menu_url_addr,
              parent_menu_no: this.menuConfDetail.parent_menu_no,
              org_parent_menu_no: this.menuConfDetailOrg.parent_menu_no,
              use_yn: this.menuConfDetail.use_yn,
              login_luna_no: this.$store.getters.getUser.luna_no
            },
            refresh: 'modifyMenuInfoSave'
          }
        },
        {
          width: 450,
          height: 167
        }
      )
    },
    createMenu () {
      if (this.menuConfDetail.menu_no) {
        this.menuDetailCreateStat = true

        this.menuConfDetailNew = Object.assign({}, this.menuConfDetail)
        this.menuConfDetailNew.menu_no = null
        this.menuConfDetailNew.menu_name = null
        this.menuConfDetailNew.menu_url_addr = null
        this.menuConfDetailNew.use_yn = 0
      }
    },

    createMenuInfoCancel () {
      this.$modal.show(
        ModalConfirm,
        {
          title: '메뉴 생성 취소',
          data: {
            url: '',
            item: {},
            refresh: 'createMenuInfoCancel'
          }
        },
        {
          width: 450,
          height: 167
        }
      )
    },
    createMenuInfoSave () {
      /* 빈값 및 기타 valid check */
      if (!this.validation(this.menuConfDetailNew, '메뉴정보 생성')) return false
      this.$modal.show(
        ModalConfirm,
        {
          title: '메뉴 생성 저장',
          data: {
            url: '/menuconf/menucreate',
            item: {
              menu_level: this.menuConfDetailNew.menu_level,
              parent_menu_no: this.menuConfDetailNew.parent_menu_no,
              menu_name: this.menuConfDetailNew.menu_name,
              use_yn: this.menuConfDetailNew.use_yn,
              menu_url_addr: this.menuConfDetailNew.menu_url_addr,
              login_luna_no: this.$store.getters.getUser.luna_no
            },
            refresh: 'createMenuInfoSave'
          }
        },
        {
          width: 450,
          height: 167
        }
      )
    },

    typing: function (evt, type, name) {
      let message = evt.target.value
      let patternKrNb = /[^ㄱ-ㅎㅏ-ㅣ가-힣0-9a-zA-Z\s`'"~!@#$%^&*-|;:?()]/
      let patternEnNb = /[^a-z0-9]/
      if (type === 'krnb') {
        if (patternKrNb.test(message)) {
          if (name === 'menu_name') this.menuConfDetail.menu_name = evt.target._value
          else if (name === 'new_menu_name') this.menuConfDetailNew.menu_name = evt.target._value
        } else {
          if (name === 'menu_name') this.menuConfDetail.menu_name = evt.target.value
          else if (name === 'new_menu_name') this.menuConfDetailNew.menu_name = evt.target.value
        }
      } else if (type === 'ennb') {
        if (patternEnNb.test(message)) {
          if (name === 'new_menu_url_addr') this.menuConfDetailNew.menu_url_addr = evt.target._value
        } else {
          if (name === 'new_menu_url_addr') this.menuConfDetailNew.menu_url_addr = evt.target.value.trim()
        }
      }
    },
    validation (menu, title) {
      let valid = []
      let level = menu.menu_level
      if (level === 'sub') {
        valid = this.valiAlertSub
      } else {
        valid = this.valiAlertMain
      }
      for (let i in valid) {
        if (!menu[i]) {
          this.$modal.show(ModalAlert,
            {
              title: title,
              text: valid[i]
            }, {
              width: 450,
              height: 'auto'
            }
          )
          return false
        }
      }
      return true
    }

  },
  components: {
    draggable,
    Ztree
  },
  props: [
    'userMenuAuth'
  ]
}
</script>

<style>
  .ztree * {
    font-size: 14px;
  }
  .ztree li {
    padding: 0;
    margin: 4px 0;
    list-style: none;
    line-height: 14px;
    text-align: left;
    white-space: nowrap;
    outline: 0
  }
  .ztree li a.curSelectedNode {
    padding-top: 1px;
    border: none;
    background-color: transparent;
    color:#37a8fe;
    font-weight: 600;
    opacity: initial;
  }
  .ztree li span {
    margin: 0 2px;
    line-height: 16px;
  }
  .ztree li span.button.ico_docu {
    display:none;
  }
</style>
