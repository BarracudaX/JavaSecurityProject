package controllers;

import dao.BankService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public String bankForm(Model model, HttpSession session){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        long currentAmount = bankService.getAmount(username);

        model.addAttribute("currentAmount", currentAmount);
        session.setAttribute("currentAmount", currentAmount);

        return "bank/transfer";
    }

    @PostMapping
    public String transfer(Model model, HttpServletRequest request) {
        String from = SecurityContextHolder.getContext().getAuthentication().getName();
        long currentAmount = bankService.getAmount(from);
        long amountToTransfer = getAmount(request,currentAmount);

        String to = request.getParameter("username");

        bankService.transfer(from, to, amountToTransfer);

        model.addAttribute("currentAmount", currentAmount);
        request.getSession().setAttribute("currentAmount", currentAmount);

        return "bank/transfer";
    }

    private long getAmount(HttpServletRequest request,long currentAmount) {
        long amountToTransfer = Long.parseLong(request.getParameter("amount"));

        if (amountToTransfer <= 0) {
            throw new IllegalArgumentException("The amount to transfer must be a positive number.");
        }

        if (amountToTransfer > currentAmount) {
            throw new IllegalArgumentException("The amount to transfer cannot be greater than the current amount.");
        }

        return amountToTransfer;
    }
}
