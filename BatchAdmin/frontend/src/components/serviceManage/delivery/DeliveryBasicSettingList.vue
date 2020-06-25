<template>
  <article class="searchResult">
    <div class="articleWrap">
        <div class="titleWrap">
          <p class="title">{{seg + '. '+ title}}</p>
        </div>
      <div class="tableWrap tableList tableGroup">
        <table :summary="title">
            <caption class="blind">{{title}}</caption>
            <colgroup>
                <col style="width:10%"/>
                <col style="width:5%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:25%"/>
                <col style="width:10%"/>
            </colgroup>
            <thead>
            <tr>
                <th class="textCenter" style="padding-left: 0px;">문의유형</th>
                <th class="textCenter" style="padding-left: 0px;">사용여부</th>
                <th class="textCenter" style="padding-left: 0px;">과실여부</th>
                <th class="textCenter" style="padding-left: 0px;">반품 택배비</th>
                <th class="textCenter" style="padding-left: 0px;">교환 택배비</th>
                <th class="textCenter" style="padding-left: 0px;">초기 택배비</th>
            </tr>
            </thead>
            <tbody>
              <template v-for="(item, idx) in dataList">
               <tr :key="item.id">
                  <td class="textCenter">
                    <template v-if="seg ===3 && item.request_no === 1">
                      <span :value="item.request_name" @click="showBasicModal" style="background: url(//static.lunasoft.co.kr/lunaadmin/images/img_info.png) 0% 50% no-repeat;padding: 0 20px 0 30px;cursor: pointer;">
                        {{item.request_name}}
                      </span>
                    </template>
                    <template v-else>
                    <span >
                       {{item.request_name}}
                    </span>
                    </template>
                  </td>
                  <td class="textCenter">
                    <span class="inputWrap">
                      <input type="checkbox" :id="seg + item + idx + 'useYn'" :true-value="1" :false-value="0" v-model="item.use_yn">
                      <label :for="seg + item + idx + 'useYn'"></label>
                    </span>
                  </td>
                  <td class="textCenter">
                  <select v-model="item.request_fault_rate">
                    <option value=100>판매자과실</option>
                    <option value=0>구매자과실</option>
                    <option value=50>판매자과실 + 구매자과실</option>
                  </select>
                </td>
                <td class="textCenter">
                  <p class="textWordWrap">
                    <input type="number" v-model.number="item.delivery_return_price"
                      @input="typing($event, 'money', item, idx, 'delivery_return_price')" v-validate="'max:10'" data-vv-as="field" name="max_field"/>
                    원
                  </p>
                </td>
                <td class="textCenter">
                  <p class="textWordWrap">
                    <input type="number" v-model="item.delivery_exchange_price"
                      @input="typing($event, 'money', item, idx, 'delivery_exchange_price')" v-validate="'max:10'" data-vv-as="field" name="max_field"/>
                    원
                  </p>
                </td>
                <td class="textCenter">

                  <span class="inputWrap">
                    <input type="checkbox" :id="seg + item + idx + 'priceYn'" :true-value="1" :false-value="0" v-model="item.delivery_free_order_price_yn">
                    <label :for="seg + item + idx + 'priceYn'">
                        <template v-if="item.delivery_free_order_price_yn === 1">
                        <span> <strong>{{defaultPrice}} </strong></span>
                      </template>
                      <template v-else>
                        <span style="color: darkgray;"> {{defaultPrice}} </span>
                      </template>
                    </label>
                  </span>
                </td>
               </tr>
               </template>

               <tr v-if="seg ===3">
                <td colspan="8" class="textRight">

                  <input class="btn_delete" type="button" value="취소" @click="cancelDialogPart">
                  <input class="btn_delete" type="button" value="적용" @click="confirmDialogPart">
                </td>
              </tr>
            </tbody>
        </table>
      </div>
    </div>
  </article>
</template>

<script>
import ModalListAlert from '@/components/common/modal/ModalListAlert'
import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  name: 'DeliveryBasicSettingList',
  props: {
    title: String,
    seg: Number,
    defaultPrice: Number | String,
    dataList: Array
  },
  data () {
    return {
      initDataList: JSON.parse(JSON.stringify(this.dataList))
    }
  },
  mounted () {
    // console.log('detail mounted')
  },
  methods: {
    lenCheck (event, lenlimt, item, idx, name) {
      // console.log('lenCheck')
      let message = event.target.value.trim()
      console.log(message)
      if (lenlimt > message.length) {
        this.dataList[idx][name] = this.initDataList[idx][name]
        // console.log(item)
        this.$modal.show(ModalAlert,
          { title: '', text: '최소 ' + lenlimt + '자 이상 기재바랍니다.' }, { width: 450, height: 'auto' }
        )
      }
    },
    typing (event, type, item, idx, name) {
      let patternNo = /^[0-9]+$/
      let message = event.target.value.trim()
      if (type === 'money') {
        if (patternNo.test(message) === false) {

        } else {
          this.dataList[idx][name] = parseInt(message)
          event.target.value = parseInt(message)
        }
      }

      if (this.errors.has('max_field')) {
        let message = event.target.value.trim()
        this.dataList[idx][name] = message.substr(0, message.length - 1)
      }
    },
    cancelDialogPart () {
      this.$parent.cancelDialog('returnPriceSetting')
    },
    confirmDialogPart () {
      this.$parent.confirmDialog('returnPriceSetting')
    },
    showBasicModal () {
      let list = []
      // if( typeof this.defaultPrice nu)
      let price = 0
      if (typeof this.defaultPrice === 'number') {
        price = this.defaultPrice
      } else {
        price = parseInt(this.defaultPrice)
      }

      list.push('1. 반품 택배비 ' + price * 2 + '원')
      list.push(' ⓐ 배송비 결제 여부 상관없이 전액 단순변심 반품시')
      list.push(' ⓑ 초기 무료 배송이였으나 단순변심 부분 반품으로 무료배송 기준 깨짐 ')
      list.push('2. 반품 택배비 ' + price + '원')
      list.push(' ⓐ 초기 무료 배송이였으며, 단순변심 부분 반품이후에도 무료배송 기준 유지')
      list.push(' ⓑ 초기 배송비 결제 하였으며, 단순변심 부분 반품 시')

      this.$modal.show(ModalListAlert,
        { title: '', list: list }, { width: 500, height: 'auto' }
      )
    }
  }
}
</script>
