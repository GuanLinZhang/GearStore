package com.interconn.demo.Controller;

import com.interconn.demo.Entity.Category;
import com.interconn.demo.Service.CategoryService;
import com.interconn.demo.vo.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("doCategoryList")
    public String doCategoryList() {
        return "/admin/view/category_list.html";
    }

    @GetMapping("getAllByPage")
    @ResponseBody
    public JSONResponse findPageObjects(@RequestParam("pageCurrent") Integer pageCurrent) {
        return new JSONResponse(1, "查询成功",
                categoryService.findPageCategories(pageCurrent));
    }

    @GetMapping("getAll")
    @ResponseBody
    public JSONResponse findAllCategories() {
        return new JSONResponse(1, "查询成功", categoryService.findAllCategories());
    }

    @PostMapping("save")
    @ResponseBody
    public JSONResponse saveCategory(Category category) {
        int result = categoryService.saveNewCategory(category);
        return result == 1 ? new JSONResponse(1, "保存成功", category) :
                new JSONResponse(0, "保存失败", category);
    }

    @PutMapping("update")
    @ResponseBody
    public JSONResponse updateCategory(Category category) {
        int result = categoryService.updateCategoryById(category);
        return result == 1 ? new JSONResponse(1, "更新成功", category) :
                new JSONResponse(0, "更新失败", category);
    }

    @DeleteMapping("delete")
    @ResponseBody
    public JSONResponse deleteCategoryLogically(Category category) {
        int result = categoryService.deleteCategory(category.getId());
        return result == 1 ? new JSONResponse(1, "删除成功", category) :
                new JSONResponse(0, "删除失败", category);
    }


}
