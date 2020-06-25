
<!-- =====================================================
 =
 =    v.0.1
 =    zTree jQuery plugin - Wrapper component
 =    updated: 2019-06-22
 =
 =    http://www.treejs.cn/v3/main.php
 =
 =
 =
 ====================================================== -->

<template>
  <ul class="ztree" ref="ztree"></ul>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import ztree from '@ztree/ztree_v3/js/jquery.ztree.core'

export default {
  name: 'ztree',
  props: {
    treeId: {
      type: [String, Number]
    },
    nodes: {
      type: Array,
      default: function () {
        return []
      }
    },
    setting: {
      type: Object,
      default: function () {
        return {}
      }
    },
    selectId: {
      type: [String, Number],
      default: ''
    },
    expandAll: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      $_treeId: '',
      $_zTree: null,
      defaults: {
        data: {
          simpleData: {
            enable: true
          }
        },
        view: {
          selectedMulti: false
        }
      }
    }
  },
  watch: {
    nodes () {
      if (this.nodes.length) this.createTree()
    },
    selectId () {
      this.selectedNode(this.selectId)
    }
  },
  methods: {
    // https://stackoverflow.com/questions/27936772/how-to-deep-merge-instead-of-shallow-merge/48579540
    mergeDeep: function (target, source) {
      const isObject = item => item && typeof item === 'object' && !Array.isArray(item) && item !== null
      if (isObject(target) && isObject(source)) {
        for (const key in source) {
          if (isObject(source[key])) {
            if (!target[key]) Object.assign(target, { [key]: {} })
            this.mergeDeep(target[key], source[key])
          } else {
            Object.assign(target, { [key]: source[key] })
          }
        }
      }
      return target
    },
    // callback 설정을 뷰 디렉티브 이벤트로 대체
    mappingCallback () {
      return Object.keys(this._events)
        .reduce((acc, key) => {
          acc[key] = this._events[key][0].fns
          return acc
        }, {})
    },
    createTree () {
      // ID(유일)로 zTree jQuery Init
      this.$_treeId = this.treeId || `zTree-${new Date().getTime()}`
      this.$refs.ztree.id = this.$_treeId
      // eslint-disable-next-line no-undef
      $.fn.zTree.init($(this.$refs.ztree), this.defaults, this.nodes)

      // zTree Object 할당
      // eslint-disable-next-line no-undef
      this.$_zTree = $.fn.zTree.getZTreeObj(this.$_treeId)

      // Node 모두 열기
      if (this.expandAll) this.$_zTree.expandAll(true)

      // 초기 Node 선택
      this.selectedNode(this.selectId)
    },
    destroyTree () {
      if (this.$_zTree) this.$_zTree.destroy()
    },
    selectedNode (id) {
      this.$_zTree.selectNode(this.$_zTree.getNodeByParam('id', id))
    }
  },
  beforeDestroy () {
    this.destroyTree()
  },
  mounted () {
    // merge data
    const setting = this.mergeDeep(this.setting, this.defaults)
    setting.callback = this.mappingCallback()
    this.defaults = setting
  },
  updated () {
    // console.log('updated', this, this.nodes)
  }
}
</script>

<style>
  @import '@ztree/ztree_v3/css/zTreeStyle/zTreeStyle.css';

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
