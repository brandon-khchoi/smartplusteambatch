<template>
  <div name="modal-alert">
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">몰 사이즈 정보 변경</div>
        <div class="content employ">
        <div class="tableWrap tableList tableHover">
          <table class="textCenter" summary="몰 사이즈 정보 변경">
            <caption class="blind">몰 사이즈 정보 변경</caption>
            <colgroup>
              <col style="width:20%" />
              <col style="width:80%" />
              <col />
            </colgroup>
            <thead>
              <tr>
                <th>이름</th>
                <th>값</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>몰 아이디</td>
                <td>{{this.mallSizeInfo.mallId}}</td>
              </tr>
              <tr>
                <td>이미지 타입</td>
                <td>
                    <span class="inputWrap">
                      <input type="checkbox" id="service05-01" value="IMAGE" v-model="mallSizeInfo.imageTypeArr">
                      <label for="service05-01">IMAGE</label>
                    </span>
                    <span class="inputWrap">
                      <input type="checkbox" id="service05-02" value="SNAPFIT" v-model="mallSizeInfo.imageTypeArr">
                      <label for="service05-02">SNAPFIT</label>
                    </span>
                    <span class="inputWrap">
                      <input type="checkbox" id="service05-03" value="CREMA" v-model="mallSizeInfo.imageTypeArr">
                      <label for="service05-03">CREMA</label>
                    </span>
                    <span class="inputWrap">
                      <input type="checkbox" id="service05-04" value="HTML" v-model="mallSizeInfo.imageTypeArr">
                      <label for="service05-04">HTML</label>
                    </span>
                    <span class="inputWrap">
                      <input type="checkbox" id="service05-05" value="CRAWLING" v-model="mallSizeInfo.imageTypeArr">
                      <label for="service05-05">CRAWLING</label>
                    </span>
                </td>
              </tr>
              <tr>
                <td>이미지 URL</td>
                <td>
                  <input type='text' v-model="mallSizeInfo.imageUrl" style="width:100%">
                </td>
              </tr>
              <tr>
                <td>클래스 명</td>
                <td>
                  <input type='text' v-model="mallSizeInfo.className" style="width:100%">
                </td>
              </tr>
              <tr>
                <td>이미지 넓이</td>
                <td>
                  <input type='text' v-model="mallSizeInfo.imageWidth" style="width:100%">
                </td>
              </tr>
              <tr>
                <td>사용 여부</td>
                <td>
                  <select v-model="mallSizeInfo.useYn" style="width:100%">
                    <option value="0">미사용</option>
                    <option value="1">사용</option>
                  </select>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        </div>
        <div class="tableTopWrap">
          <div class="btnWrap">
            <ul>
              <li>
                <p class="btn_add" @click="save()">저장</p>
              </li>
            </ul>
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
import { eventBus } from '@/main'
import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  data () {
    return {
      mallSizeInfo: {}
    }
  },
  mounted () {
    this.get()
  },
  methods: {
    async get () {
      http
        .get('/mallSizeInfo/getMallSizeInfo', {
          params: { // query string
            mallId: this.mallId
          }
        })
        .then(result => {
          this.mallSizeInfo = result.data.data
          this.mallSizeInfo.imageTypeArr = this.mallSizeInfo.imageType.split('|')
        })
        .catch(e => {
          console.log(e)
        })
    },
    close () {
      this.$emit('close')
    },
    save () {
      this.mallSizeInfo.imageType = this.mallSizeInfo.imageTypeArr.join('|')

      if (this.mallSizeInfo.imageType === '') {
        this.$modal.show(ModalAlert,
          { title: '', text: '이미지 타입은 필수 입니다.' }, { width: 450, height: 'auto' }
        )
        return
      }

      http
        .post('/mallSizeInfo/updateMallSizeInfo', JSON.stringify(this.mallSizeInfo))
        .then(result => {
          if (result.data.responseCode === 0) {
            this.$modal.show(ModalAlert,
              { title: '', text: '업데이트에 성공하였습니다.' }, { width: 450, height: 'auto' }
            )
            this.close()
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: result.data.responseMsg }, { width: 450, height: 'auto' }
            )
          }
          eventBus.$emit('saveMallSizeInfo')
        })
        .catch(e => {
          this.$modal.show(ModalAlert,
            { title: '', text: e.response.data.responseMsg }, { width: 450, height: 'auto' }
          )
        })
    }
  },
  props: {
    'mallId': String
  }
}
</script>
