<template>
  <div name="modal-alert">
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">배치 그룹 관리</div>
        <div class="content employ">
          <div class="tableWrap tableList tableHover">
            <table class="textCenter" summary="배치리스트">
              <caption class="blind">배치리스트</caption>
              <colgroup>
                <col style="width:55%"/>
                <col style="width:10%"/>
                <col style="width:20%"/>
                <col style="width:15%"/>
                <col/>
              </colgroup>
              <thead>
              <tr>
                <th>이름</th>
                <th>순서</th>
                <th>사용여부</th>
                <th>관리</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, idx) in batchGroupList" :key="idx">
                <td>
                  <input type="text" v-model="item.batch_group_name" style="width:90%"/>
                </td>
                <td>
                  <input type="text" v-model="item.display_no" style="width:90%"/>
                </td>
                <td>
                  <select v-model="item.use_yn" style="width:90%">
                    <option :value="1">사용</option>
                    <option :value="0">미사용</option>
                  </select>
                </td>
                <td>
                  <p
                    class="btn_search"
                    style="width:40%;"
                    @click="createNewGroup()"
                  >추가</p>
                  <p
                    class="btn_search"
                    style="width:40%;"
                    @click="deleteBatchGroup(item.batch_group_no, idx)"
                  >삭제</p>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="tableTopWrap">
          <div class="btnWrap" style="float:left">
            <ul>
              <li>
                <p class="btn_add" @click="close()">취소</p>
              </li>
            </ul>
          </div>
          <div class="btnWrap">
            <ul>
              <li>
                <p class="btn_add" @click="save()">적용</p>
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
import {eventBus} from '@/main'
import http from '../../http-commons'
import store from '../../vuex/store'
import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  data () {
    return {
      lunaNo: store.getters.getUser.luna_no,
      lunaTeamNo: store.getters.getUser.luna_team_no,
      groupName: '',
      selectedGroupId: '',
      batchGroupList: []
    }
  },
  mounted () {
    this.getbatchGroupList()
  },
  methods: {
    getbatchGroupList () {
      let form = new FormData()
      form.append('lunaTeamNo', this.lunaTeamNo)

      http
        .post('/batchManage/allBatchGroupList', form)
        .then(result => {
          this.batchGroupList = result.data
          if (this.batchGroupList.length === 0) {
            this.createNewGroup()
          }
        })
        .catch(e => {
          console.log(e)
        })
    },
    groupSelect () {
      if (this.selectedGroupId === '') {
        this.groupName = ''
      } else {
        for (const idx in this.batchGroupList) {
          if (this.batchGroupList[idx].batch_group_no === this.selectedGroupId) {
            this.groupName = this.batchGroupList[idx].batch_group_name
            break
          }
        }
      }
    },
    close () {
      this.$emit('close')
    },
    createNewGroup () {
      var item = {}

      item.batch_group_no = ''
      item.create_user_code = '1'
      item.create_user_no = this.lunaNo
      item.use_yn = '1'
      item.batch_group_name = ''
      item.luna_team_no = this.lunaTeamNo

      let tempDispluaNo = 0
      for (const idx in this.batchGroupList) {
        if (tempDispluaNo < this.batchGroupList[idx].display_no) {
          tempDispluaNo = this.batchGroupList[idx].display_no
        }
      }
      item.display_no = tempDispluaNo + 1
      this.batchGroupList.push(item)
    },
    deleteBatchGroup (groupNo, idx) {
      if (groupNo === '') {
        this.batchGroupList.splice(idx, 1)
      } else {
        http
          .get('/batchManage/deleteBatchGroup', {
            params: {
              groupNo: groupNo
            }
          })
          .then(result => {
            if (result.data === 'success') {
              this.$modal.show(ModalAlert,
                {title: '', text: '배치 그룹 삭제에 성공하였습니다.'}, {width: 450, height: 'auto'}
              )
              eventBus.$emit('saveBatchGroup')
            } else {
              this.$modal.show(ModalAlert,
                {title: '', text: '배치 그룹 삭제에 실패하였습니다.'}, {width: 450, height: 'auto'}
              )
            }
            setTimeout(() => {
              this.getbatchGroupList()
            }, 100)
          })
          .catch(e => {
            console.log(e)
          })
      }
    },
    save () {
      for (const idx in this.batchGroupList) {
        if (this.batchGroupList[idx].batch_group_name === '') {
          this.$modal.show(ModalAlert,
            {title: '', text: '배치 그룹 명은 필수 값입니다.'}, {width: 450, height: 'auto'}
          )
          return
        } else if (this.batchGroupList[idx].display_no === '') {
          this.$modal.show(ModalAlert,
            {title: '', text: '배치 순서는 필수 값입니다.'}, {width: 450, height: 'auto'}
          )
          return
        }

        if (this.batchGroupList[idx].batch_group_no !== '') {
          this.batchGroupList[idx].update_user_code = '1'
          this.batchGroupList[idx].update_user_no = this.lunaNo
        }
      }

      let context = this

      this.saveGroupSend()
        .then(function (result) {
          if (result.data === 'success') {
            context.$modal.show(ModalAlert,
              {title: '', text: '그룹 저장에 성공하였습니다.'}, {width: 450, height: 'auto'}
            )
            eventBus.$emit('saveBatchGroup')
            context.$emit('close')
          } else {
            context.$modal.show(ModalAlert,
              {title: '', html: result.data}, {width: 450, height: 'auto'}
            )
            eventBus.$emit('saveBatchGroup')
            context.getbatchGroupList()
          }
        })
    },
    async saveGroupSend (batchGroupInfo) {
      return new Promise(resolve => {
        http
          .post('/batchManage/saveBatchGroup', JSON.stringify(this.batchGroupList))
          .then(response => {
            resolve(response)
          }).catch(e => {
            console.log(e)
          })
      })
    }
  }
}
</script>
