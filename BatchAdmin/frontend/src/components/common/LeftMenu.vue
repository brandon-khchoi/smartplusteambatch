<!--
  2019.07.01

  updated: Leo

  :TODO
  common.js -> nav tag 제거
  common.css -> nav 제거
-->

<template>
 <div class="sidebar">
   <div class="sidebarWrap">
   <span class="toggle" @click="onToggleSidebar">
     <span>
       <i class="fas fa-caret-left" />
     </span>
   </span>
     <div class="profile">
       <ul>
         <li class="team">
           <span>
             <div>
               <span v-if="$store.getters.getUser.luna_dept_name != null"> {{$store.getters.getUser.luna_dept_name}}</span>
               <span v-if="$store.getters.getUser.luna_team_name != null">/{{$store.getters.getUser.luna_team_name}}</span>
             </div>
             <div v-if="$store.getters.getUser.luna_part_name != null">/{{$store.getters.getUser.luna_part_name}}</div>
           </span>
           <span>{{$store.getters.getUser.luna_id}}</span>
         </li>
         <li class="name">
           <span>{{$store.getters.getUser.luna_eng_kor_nick_name}}</span>
           <span>{{$store.getters.getUser.luna_eng_nick_name}}</span>
         </li>
       </ul>
     </div>
     <div class="nav">
       <ul>
         <li @click="onMainClick(0)"
             :class="{ opened: isSelectedMain(0) }"
         >
           <h2>
             <i class="icon fa fa-cubes"></i>
               <span>배치관리자</span>
               <span>
                 <i class="fas fa-chevron-down arrow">
                 </i>
               </span>
           </h2>
             <vue-slide-toggle
               :open="isSelectedMain(0)"
               :duration="200"
               tag="div">
             <ul>
               <li class="active" @click.stop="onSubClick(0,0, 'batch')">
                <a>배치상세</a>
               </li>
             </ul>
           </vue-slide-toggle>
         </li>
<!--        <li-->
<!--           v-for="mItem in menuList"-->
<!--           @click="onMainClick(mItem.menu_main_no)"-->
<!--           :class="{ opened: isSelectedMain(mItem.menu_main_no) }"-->
<!--           :key="mItem.menu_main_no">-->
<!--           <h2>-->
<!--             <i class="icon" :class="[mItem.icon]"></i>-->
<!--             <span>{{ mItem.menu_main_name }}</span>-->
<!--             <span>-->
<!--                <i-->
<!--                  v-if="!isHomeMenu(mItem.menu_main_no)"-->
<!--                  class="fas fa-chevron-down arrow">-->
<!--                </i>-->
<!--             </span>-->
<!--           </h2>-->
<!--           <vue-slide-toggle-->
<!--            :open="isSelectedMain(mItem.menu_main_no)"-->
<!--            :duration="200"-->
<!--            tag="div">-->
<!--            <ul v-if="!!mItem.subMenu.length">-->
<!--              <li-->
<!--                v-for="sItem in mItem.subMenu"-->
<!--                @click.stop="onSubClick(mItem.menu_main_no, sItem.menu_sub_no, sItem.menu_url_addr)"-->
<!--                :class="{ active : isSelectedSub(sItem.menu_sub_no) }"-->
<!--                :key="sItem.menu_sub_no">-->
<!--                <a> {{sItem.menu_sub_name}} </a>-->
<!--              </li>-->
<!--            </ul>-->
<!--           </vue-slide-toggle>-->
<!--         </li>-->
       </ul>
     </div>
   </div>
 </div>
</template>

<script>
import { VueSlideToggle } from 'vue-slide-toggle'
import { eventBus } from '../../main'
import http from '../../http-commons'
import ModalAlert from './modal/ModalAlert.vue'

export default {
  components: {
    'vue-slide-toggle': VueSlideToggle
  },
  created () {
    this.menuList = this.$store.getters.getUserMenuMain.map(mItem => {
      const icon = (() => {
        switch (mItem.menu_main_no) {
          case 1: return 'fa fa-home' // Home
          case 2: return 'fa fa-cog' // Management
          case 3: return 'fa fa-user' // User
          case 4: return 'fa fa-signal' // Business
          case 5: return 'fas fa-cubes' // Service
          default: return 'fa fa-cubes'
        }
      })()
      const subMenu = this.$store.getters.getUserMenuSub.filter(sItem => {
        return mItem.menu_main_no === sItem.menu_main_no
      })
      return { ...mItem, subMenu, icon }
    })

    eventBus.$on('leftMenuOpenSelectedMain', () => {
      this.isOpenedMain = true
      this.isOpenDataSync = true
    })
    eventBus.$on('leftMenuCloseSelectedMain', () => {
      this.isOpenedMain = false
      this.isOpenDataSync = false
    })
  },
  props: {
    openedSidebar: {
      type: Boolean,
      default: true
    },
    onToggleSidebar: {
      type: Function,
      default: function () {
        console.wran('not defined function')
      }
    }
  },
  data () {
    return {
      selectedMainIndex: Number(localStorage.getItem('selectedMainIndex') || 1),
      selectedSubIndex: Number(localStorage.getItem('selectedSubIndex') || -1),
      selectedUrl: localStorage.getItem('selectedUrl') || '',
      isOpenedMain: true,
      isOpenDataSync: true, // 회원데이터 동기화 박스의 lnb 오픈여부에 따른 처리
      isShowDataSync: false, // 회원데이터 동기화 박스의 활성/비활성 처리
      dataSyncMemId: '', // 회원데이터 동기화 박스의 활성/비활성 처리
      dataSyncDupChk: true, // 회원데이터 동기화 중복처리 방지
      menuList: []
    }
  },
  methods: {
    saveFinalMenu_LS () {
      localStorage.setItem('selectedMainIndex', this.selectedMainIndex)
      localStorage.setItem('selectedSubIndex', this.selectedSubIndex)
      localStorage.setItem('selectedUrl', this.selectedUrl)
    },
    isSelectedMain (mainIndex) {
      return this.isOpenedMain && mainIndex === this.selectedMainIndex
    },
    onMainClick (mainIndex) {
      if (!this.openedSidebar) this.onToggleSidebar()

      if (mainIndex === this.selectedMainIndex) {
        this.isOpenedMain = !this.isOpenedMain
      } else {
        this.isOpenedMain = true
        this.selectedMainIndex = mainIndex
      }

      if (mainIndex === 1) {
        this.$router.push({ name: 'dashboard' })
        this.saveFinalMenu_LS()
      }
    },
    onSubClick (mainIndex, subIndex, subUrl) {
      let urlPattern = /(http:|https:|www\.)/g
      this.selectedMainIndex = mainIndex
      this.selectedSubIndex = subIndex
      this.selectedUrl = subUrl
      this.saveFinalMenu_LS()

      // selectedUrl 가 $router의 name이 아닌 url인경우 외부페이지를 열기위한 분기
      if (urlPattern.test(this.selectedUrl)) {
        // eslint-disable-next-line standard/object-curly-even-spacing
        this.$router.push({ name: 'emptypage', params: { 'pageurl': this.selectedUrl}})
      } else {
        this.$router.push({ name: this.selectedUrl ? this.selectedUrl : 'dashboard' })
      }
    },
    isSelectedSub (selectedSub) {
      return selectedSub === this.selectedSubIndex
    },
    isHomeMenu (mainMenuIndex) {
      return mainMenuIndex === 1
    },
    dataSync () {
      if (this.dataSyncMemId !== '' && this.dataSyncDupChk) {
        this.dataSyncDupChk = false
        http
          .get('/datasync/oldtonew', {
            headers: {
              apiKey: '617e3c05ff5debf757676b76a12e5caacb118ca27c2c8ecec15ab3e4600866a4'
            },
            params: {
              memId: this.dataSyncMemId
            }
          })
          .then(result => {
            this.dataSyncDupChk = true
            let msg = ''
            if (result.data.rtnCode === '0000') {
              this.mallInfoCache()
              msg = '정보가 갱신되었습니다.'
            } else if (result.data.rtnCode === '0003') {
              msg = '미등록 회원입니다.'
            } else {
              msg = '처리에 실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '회원정보 가져오기',
                text: msg
              }, {
                width: 450,
                height: 167
              }
            )
          })
          .catch(e => {
            this.dataSyncDupChk = true
            console.log('LeftMenu.vue(dataSync) : ' + e)
          })
      }// if
    },
    mallInfoCache () { // tb_mall_info 를 redis에 등록처리
      if (this.dataSyncMemId !== '') {
        http
          .get('/memory/mallinfo/' + this.dataSyncMemId)
          .catch(e => {
            console.log('mallInfoCache : ' + e)
          })
      }
    },
    enFilter: function (evt) {
      let patternKr = /[ㄱ-ㅎㅏ-ㅣ가-힣]/
      let patternSp = /[~!@#$%^&*()<>=+’"'{};]/
      let txt = evt.target.value

      if (patternKr.test(txt) || patternSp.test(txt)) {
        this.dataSyncMemId = evt.target._value
      } else {
        this.dataSyncMemId = txt.trim()
      }
    }
  }
}
</script>

<style scoped>
  .nav {
    background:#313d4a;
    user-select: none;
  }
  .nav > ul > li {
    clear: both;
    border-top: 1px solid #2f3740;
    min-height: 62px;
    cursor: pointer;
  }
  .nav > ul > li:last-child{
    border-bottom: 1px solid #2f3740;
  }
  .nav > ul > li.opened {
    background: #222a34;
  }
  .nav > ul > li h2 {
    overflow: hidden;
  }
  .nav > ul > li.opened h2 span {
    color:#37a8fe;
    font-weight:600;
  }
  .nav > ul > li.opened h2 > span:last-child {
    color:#fff;
  }
  .nav > ul > li.opened h2 {
    /* overflow: initial; */
  }
  .nav > ul > li h2 > i {
    float: left;
    width: 60px;
    height: 39px;
    margin-top: 23px;
    font-size: 16px;
    text-align: center;
  }
  .nav > ul > li.opened h2 span:last-child i {
    -webkit-transform: rotate(180deg);
    -moz-transform: rotate(180deg);
    transform: rotate(180deg);
    -o-transform: rotate(180deg);
    -ms-transform: rotate(180deg);
  }
  .nav > ul > li h2 span {
    float:left;
    width:110px;
    line-height:62px;
    font-size:14px;
    color:#8696a3;
    font-weight:400;
    text-align:left;
  }
  .nav > ul > li h2 span:last-child {
    float:left;
    width:50px;
    text-align:center;
  }
  .nav > ul > li h2 span:last-child i {
    transition: all 0.3s ease 0s; margin-top:-2px;
  }
  .nav > ul > li ul {
    padding: 10px 0 10px 60px;
  }
  .nav > ul > li ul li a {
    line-height:40px;
    color:#62707f;
  }
  .nav > ul > li ul li.active a, .nav > ul > li ul li:hover a {
    color:#fff;
    font-weight: 500;
  }
  .container.menuClosed .sidebar .profile, .container.menuClosed .sidebar .nav ul li > h2 span, .container.menuClosed .sidebar .nav ul li > h2 + ul {
    display:none !important;
  }
  .container.menuClosed .sidebar .nav ul li > h2 > i {
    width:75px;
  }

</style>
