package tech.anubislab.ebankingbackend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.anubislab.ebankingbackend.entities.BankAccount;
import tech.anubislab.ebankingbackend.entities.CurrentAccount;
import tech.anubislab.ebankingbackend.entities.SavingAccount;
import tech.anubislab.ebankingbackend.repositories.BankAccountRepository;

@Service
@Transactional
public class BankService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("25bdb54d-df28-410d-94ef-61e7cba52ede").orElse(null);
        if (bankAccount==null){
            System.out.println("Cet identifiant n'existe pas");
        }else {
            System.out.println("******************************************************");
            System.out.println("Identifiant: "+bankAccount.getId());
            System.out.println("Balance: "+bankAccount.getBalance());
            System.out.println("Status: "+bankAccount.getStatus());
            System.out.println("Création: "+bankAccount.getCreatedAt());
            System.out.println("Propiétaire: "+bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount){
                System.out.println("En découvert => "+((CurrentAccount)bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Taux d'interet => "+((SavingAccount)bankAccount).getInterestRate());
            }
            System.out.println("============= Opérations sur le Compte =============");
            bankAccount.getAccountOperations().forEach(op->{
                System.out.println(op.getType() +"\t"+ op.getAmount()+ "\t" +op.getOperationDate());
            });
        }
    }
}
