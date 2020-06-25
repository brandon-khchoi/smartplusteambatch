<template>
  <div class="content partner">
    <h1>
      공지사항 상세
    </h1>
    <div class="formContainer">
      <form action="#" method="post">
        <div class="writeContainer">
          <article>
            <div class="articleWrap">
              <div class="titleWrap">
                <p class="title" >[{{ this.codeData.noticeType[noticeInfo.noticeTypeCode] }}] {{ noticeInfo.noticeTitle }}</p>
                <ul class="infoWrap">
                  <li class="writerWrap" v-if="noticeInfo.importantYn == '1'">중요</li>
                  <li class="writerWrap" v-if="noticeInfo.displayYn == '0'"><i class="far fa-eye-slash"></i></li>
                  <li class="writerWrap" v-else><i class="fas fa-eye"></i></li>
                  <li class="timeWrap">{{ noticeInfo.createDate }}</li>
                </ul>
              </div>
              <div >
                <table summary="공지사항 미리보기">
                  <caption class="blind">공지사항 미리보기</caption>
                  <colgroup>
                    <col style="width:100px" />
                    <col />
                  </colgroup>
                  <tbody>
                    <tr>
                      <td colspan="2" class="scrollContainer">
                        <div class="scrollWrap" style="height: 500px;" v-html="noticeInfo.noticeText">
                        </div>
                      </td>
                    </tr>
                      <tr>
                        <td colspan="2">
                          <table>
                            <tr>
                              <th>첨부파일</th>
                            </tr>
                            <template v-for="(item, idx) in noticeFileInfo">
                              <tr :key="idx">
                                <td>
                                  <a @click="download(item.noticeFileNo, item.noticeFileName)">
                                    <i class="fas fa-paperclip"></i> {{ item.noticeFileName }}
                                  </a>
                                </td>
                              </tr>
                            </template>
                          </table>
                        </td>
                      </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </article>
          <div class="btnWrap">
            <ul>
              <li>
                <a @click="goList()" class="btn_cancel">목록으로</a>
              </li>
              <li>
                <a @click="noticeDeleteConfirm()" class="btn_save" >삭제</a>
              </li>
              <li>
                <a @click="noticeEdit()" class="btn_save" >수정</a>
              </li>
            </ul>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import http from '../../../http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalConfirm from '@/components/common/modal/ModalConfirm'
import { eventBus } from '@/main'

export default {
  data () {
    return {
      noticeInfo: {},
      noticeFileInfo: []
    }
  },
  mounted () {
    eventBus.$on('deleteNotice', () => {
      this.deleteNotice()
    })
    if (this.$route.query.noticeNo !== undefined) {
      this.getNoticeInfo()
    }
  },
  beforeDestroy () {
    eventBus.$off('deleteNotice')
  },
  methods: {
    noticeDeleteConfirm () {
      this.$modal.show(
        ModalConfirm,
        {
          title: '공지사항 삭제',
          data: {
            url: '',
            refresh: 'deleteNotice'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
    },
    deleteNotice () {
      http
        .get('/notice/delete', {
          params: {
            noticeNo: this.$route.query.noticeNo
          }
        }).then(response => {
          if (response.data.responseCode === 0) {
            this.$modal.show(ModalAlert,
              { title: '', text: '공지사항이 삭제되었습니다.' }, { width: 450, height: 'auto' }
            )
          }
          this.goList()
        }).catch(e => {

        })
    },
    getNoticeInfo () {
      http
        .get('/notice/getNoticeInfo', {
          params: {
            noticeNo: this.$route.query.noticeNo
          }
        }).then(response => {
          this.noticeInfo = response.data.data.noticeInfo
          this.noticeFileInfo = response.data.data.noticeFileInfo
        }).catch(e => {
          console.log(e)
        })
    },
    download (fileNo, fileName) {
      // eslint-disable-next-line no-undef
      const url = imageApiUrl + '/notice/getNoticeFile'

      http
        .get(url, {
          responseType: 'blob',
          params: {
            noticeFileNo: fileNo
          }
        })
        .then(response => {
          var fileURL = window.URL.createObjectURL(new Blob([response.data]))
          var fileLink = document.createElement('a')
          fileLink.href = fileURL
          fileLink.setAttribute('download', fileName)
          document.body.appendChild(fileLink)
          fileLink.click()
        })
        .catch(e => {
          console.log(e)
        })
    },
    goList () {
      this.$router.push({ name: 'noticeList' })
    },
    noticeEdit () {
      this.$router.push({ name: 'noticeEdit',
        params: {'noticeInfo': this.noticeInfo, 'noticeFileInfo': this.noticeFileInfo}
      })
    }
  },
  props: {
    noticeNo: Number,
    'userMenuAuth': Object,
    'codeData': Object
  }
}
</script>
