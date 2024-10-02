package sg.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.funding.AdminSummaryDto;
import sg.backend.entity.Funding;
import sg.backend.entity.State;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminSummaryService {

    private final UserRepository userRepository;
    private final FundingRepository fundingRepository;
    private final FundingLikeRepository fundingLikeRepository;

    @Transactional
    public AdminSummaryDto getAdminSummary() {
        long totalUsers = userRepository.count();
        long todayUsers = userRepository.countByCreatedAtAfter(LocalDate.now().atStartOfDay());  //오늘가입수

        double averageFundingLikes = calculateAverageFundingLikes(totalUsers);
        double averageFundingAmount = calculateAverageAmount();

        long totalFundings = fundingRepository.count();
        long closedFundings = fundingRepository.countByCurrent(State.CLOSED);

        long ongoingFundings = fundingRepository.countByCurrent(State.ONGOING);
        long successfulFundings = calculateSuccessfulFundings();
        long failedFundings = closedFundings - successfulFundings;

        double successRate = calculateSuccessRate(closedFundings, successfulFundings);

        return new AdminSummaryDto(totalUsers, todayUsers, averageFundingLikes,
                        averageFundingAmount, totalFundings, closedFundings,
                successRate, ongoingFundings, successfulFundings, failedFundings);
    }

    private double calculateAverageFundingLikes(long totalUsers) {
        double averageLikes = (totalUsers > 0) ? (double) fundingLikeRepository.count() / totalUsers : 0;
        return Math.round(averageLikes * 10) / 10.0;
    }

    private double calculateAverageAmount(){
        List<Funding> fundings = fundingRepository.findAll();
        double totalAmount = fundings.stream()
                .mapToDouble(Funding::getCurrentAmount)
                .sum();
        double averageAmount = (!fundings.isEmpty()) ? totalAmount / fundings.size() : 0;
        return Math.round(averageAmount * 10) / 10.0;
    }

    private long calculateSuccessfulFundings() {
        List<Funding> closedFundingList = fundingRepository.findByCurrent(State.CLOSED);
        return closedFundingList.stream()
                .filter(funding -> funding.getCurrentAmount() >= funding.getTargetAmount())
                .count();
    }

    private int calculateSuccessRate(long closedFundings, long successfulFundings) {
        return (closedFundings > 0) ? (int) ((double) successfulFundings / closedFundings * 100) : 0;
    }
}
