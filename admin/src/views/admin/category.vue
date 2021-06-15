<template>
  <div>
    <div class="row">
      <div class="col-md-6">
        <p>
          <button v-on:click="add1()" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-edit"></i>
            新增一级
          </button>
          &nbsp;
          <button v-on:click="all()" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-refresh"></i>
            刷新
          </button>
        </p>

        <table id="level1-table" class="table  table-bordered table-hover">
          <thead>
          <tr>
            <th>id</th>
            <th>名称</th>
            <th>顺序</th>
            <th>操作</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="category in level1" v-on:click="onClickLevel1(category)"
              v-bind:class="{'active': category.id === active.id}">
            <td>{{ category.id }}</td>
            <td>{{ category.name }}</td>
            <td>{{ category.sort }}</td>
            <td>
              <div class="btn-group">
                <button v-on:click="edit(category)" class="btn btn-white btn-xs btn-info btn-round">
                  编辑
                </button>&nbsp;
                <button v-on:click="del(category.id)" class="btn btn-white btn-xs btn-warning btn-round">
                  删除
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="col-md-6">
        <p>
          <button v-on:click="add2()" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-edit"></i>
            新增二级
          </button>
        </p>

        <table id="level2-table" class="table  table-bordered table-hover">
          <thead>
          <tr>
            <th>id</th>
            <th>名称</th>
            <th>顺序</th>
            <th>操作</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="category in level2">
            <td>{{ category.id }}</td>
            <td>{{ category.name }}</td>
            <td>{{ category.sort }}</td>
            <td>
              <div class="btn-group">
                <button v-on:click="edit(category)" class="btn btn-white btn-xs btn-info btn-round">
                  编辑
                </button>&nbsp;
                <button v-on:click="del(category.id)" class="btn btn-white btn-xs btn-warning btn-round">
                  删除
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label">父分类</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{ active.name || "无" }}</p>
                </div>
              </div>
              <div class="form-group">
                <label for="name" class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="category.name" type="text" class="form-control" id="name" placeholder="名称">
                </div>
              </div>
              <div class="form-group">
                <label for="sort" class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="category.sort" type="text" class="form-control" id="sort" placeholder="顺序">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>

</template>

<script>

export default {
  name: "business-category",
  data: function () {
    return {
      category: {}, // 用于绑定form表单的数据
      categorys: [],
      level1: [],
      level2: [],
      active: {}
    }
  },
  mounted: function () {
    // sidebar激活样式方法一
    // this.$parent.activeSidebar("business-category-sidebar");
    let _this = this;
    _this.all();
  },
  methods: {
    /**
     * 点击编辑
     * @param category
     */
    edit(category) {
      let _this = this;
      _this.category = $.extend({}, category); // 列表数据复制一份赋值给表单category，防止修改表单时把列表也修改
      $("#form-modal").modal("show");//hide
    },
    /**
     * 点击新增
     */
    add() {
      let _this = this;
      //.modal里的modal是内置的方法,用于弹出或关闭模态框
      _this.category = {};
      $("#form-modal").modal("show");//hide
    },
    /**
     * 新增一级
     */
    add1() {
      let _this = this;
      _this.active = {};
      _this.level2 = [];
      _this.category = {
        parent: "00000000"
      };
      $("#form-modal").modal("show");//hide
    },
    /**
     * 新增二级
     */
    add2() {
      let _this = this;
      //只有触发一级表格点击事件，active才有值
      if (Tool.isEmpty(_this.active)) {
        Toast.warning("请先点击一级分类");
        return;
      }
      _this.category = {
        parent: _this.active.id
      };
      $("#form-modal").modal("show");//hide
    },
    /**
     * 列表查询
     */
    all() {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/category/all").then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.categorys = resp.content;
        // 将所有记录格式化成树形结构
        _this.level1 = [];
        for (let i = 0; i < _this.categorys.length; i++) {
          let c = _this.categorys[i];
          if (c.parent === '00000000') {
            //每次查询时都会往level1push，所以应该在格式化之前将level1清空
            _this.level1.push(c);
            for (let j = 0; j < _this.categorys.length; j++) {
              let child = _this.categorys[j];
              if (child.parent === c.id) {
                if (Tool.isEmpty(c.children)) {
                  c.children = [];
                }
                c.children.push(child);
              }
            }
          }
        }
        _this.level2 = [];
        // 对当前一级分类中选中的表格触发一次点击事件，以刷新二级菜单列表
        // 注意：界面的渲染需要等vue绑定好变量后才做，所有加延时100ms
        setTimeout(function () {
          $("tr.active").trigger("click");
        }, 100);
      });
    },
    /**
     * 点击保存
     */
    save() {
      let _this = this;

      // 保存校验
      if (1 != 1
          || !Validator.require(_this.category.parent, "父id")
          || !Validator.require(_this.category.name, "名称")
          || !Validator.length(_this.category.name, "名称", 1, 50)
      ) {
        return;
      }
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/category/save", _this.category).then((response) => {
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.all();
          Toast.success("保存成功");
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    /**
     * 点击删除
     * @param id
     */
    del(id) {
      let _this = this;
      Confirm.show("确认删除？", "删除后不可恢复，确认删除!", function () {
        _this.$ajax.delete(process.env.VUE_APP_SERVER + "/business/admin/category/delete/" + id).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.all();
            Toast.success("删除成功");
          }
        })
      });
    },
    /**
     *
     * @param category
     */
    onClickLevel1(category) {
      let _this = this;
      _this.active = category;
      _this.level2 = category.children;
    }
  }
}
</script>

<style scoped>
.active td {
  background-color: #d6e9c6 !important;
}
</style>