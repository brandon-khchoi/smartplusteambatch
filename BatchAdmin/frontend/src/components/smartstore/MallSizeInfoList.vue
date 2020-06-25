<template>
  <div class="content">
    <h1>몰 별 사이즈 정보 관리</h1>
    <div class="formContainer">
      <div class="searchWrap">
        <CommonServiceList :propMallNoQuery= $route.query.mall_no></CommonServiceList>
      </div>
      <div class="loading">
        <article class="searchResult" >
          <div class="articleWrap">
            <div class="tableTopWrap">
            </div>
            <div class="tableWrap tableList tableGroup">
              <table summary="몰 별 사이즈 정보 관리">
                <caption class="blind">몰 별 사이즈 정보 관리</caption>
                <colgroup>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:28%"/>
                    <col style="width:28%"/>
                    <col style="width:6%"/>
                    <col style="width:7%"/>
                    <col style="width:6%"/>
                </colgroup>
                <thead>
                <tr>
                  <th >몰 아이디</th>
                  <th >이미지 타입</th>
                  <th >이미지 url</th>
                  <th >사용 클래스 명</th>
                  <th >이미지 넓이</th>
                  <th >사용 여부</th>
                  <th >저장</th>
                </tr>
                </thead>
                <tbody>
                  <template v-if="this.mallSizeInfo.data !== ''">
                  <tr >
                    <td>
                       {{ this.mallSizeInfo.data.mallId }}
                    </td>
                    <td >
                      {{this.mallSizeInfo.data.imageType}}
                    </td>
                    <td>
                      {{this.mallSizeInfo.data.imageUrl}}
                    </td>
                    <td >
                      {{this.mallSizeInfo.data.className}}
                    </td>
                    <td >
                      {{this.mallSizeInfo.data.imageWidth}}
                    </td>
                    <td>
                      {{ this.mallSizeInfo.data.useYn === '0' ? '미사용' : '사용' }}
                    </td>
                    <td>
                       <div class="tableTopWrap" style="padding-top: 0px;" >
                          <div class="btnWrap" style="width:90%">
                            <ul>
                              <li style="width:100%">
                                <p class="btn_add" style="width:100%" @click="modify()">수정</p>
                              </li>
                            </ul>
                          </div>
                        </div>
                    </td>
                  </tr>
                  </template>
                  <template v-else>
                    <tr >
                      <td colspan="7" class="textCenter" >
                        조회된 내용이 없습니다.
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>

          </div>
        </article>
      </div>
    </div>
    <v-dialog />
    <Spinner v-if="isLoading" />
  </div>

</template>

<script>
import http from '@/http-commons'
import CommonServiceList from './CommonServiceList'
import { eventBus } from '@/main'
import MallSizeInfoEdit from '@/components/smartstore/MallSizeInfoEdit'

export default {
  name: 'MallSizeInfoList',
  components: {
    CommonServiceList
  },
  data () {
    return {
      isLoading: false,
      mallSizeInfo: {
        data: ''
      }
    }
  },
  mounted () {
    eventBus.$on('commonServiceListChange', () => {
      this.get()
    })
    eventBus.$on('saveMallSizeInfo', (result) => {
      setTimeout(() => {
        this.get()
      }, 100)
    })
  },
  beforeDestroy () {
    eventBus.$off('saveMallSizeInfo')
    eventBus.$off('commonServiceListChange')
  },
  methods: {
    async get (page, perPage) {
      http
        .get('/mallSizeInfo/getMallSizeInfo', {
          params: { // query string
            mallId: this.$store.state.searchParam.mall_id
          }
        })
        .then(result => {
          this.mallSizeInfo.data = result.data.data
        })
        .catch(e => {
          console.log(e)
        })
    },
    modify () {
      this.$modal.show(MallSizeInfoEdit,
        {mallId: this.mallSizeInfo.data.mallId}, { width: 700, height: 'auto' }
      )
    },
    getUseDesc (useYn) {
      if (useYn === '1') {
        return '사용'
      } else {
        return '미사용'
      }
    }
  }
}

</script>
