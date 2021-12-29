package com.main.sellplatform.service;

import com.main.sellplatform.controller.dto.lotdto.LotFilterDTO;
import com.main.sellplatform.entitymanager.analyzer.Queries;
import com.main.sellplatform.entitymanager.testdao.LotDao2;
import com.main.sellplatform.entitymanager.testobj.User;
import com.main.sellplatform.persistence.dao.LotDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Lot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LotService {
    private final UserDao userDao;

    private final LotDao lotDao;
    private final LotDao2 lotDao2;
    private final Queries queries;

    @Autowired
    public LotService(UserDao userDao, LotDao lotDao, LotDao2 lotDao2, Queries queries) {
        this.userDao = userDao;
        this.lotDao = lotDao;
        this.lotDao2 = lotDao2;
        this.queries = queries;
    }

    private String getLotSortCol(LotFilterDTO.Column col) {
        if (col == null) return null;
        switch (col) {
            //case "Id":
            //    return "OBJ_3ATTR_10";
            case NAME:
                return "OBJ_3ATTR_12";
            //case "Status":
            //    return "OBJ_3ATTR_17";
            case CATEGORY:
                return "OBJ_3ATTR_18";
            case PRICE:
                return "OBJ_3ATTR_14";
            //case "StartDate":
            //    return "OBJ_3ATTR_21";
            //case "EndDate":
            //    return "OBJ_3ATTR_22";
            //case "StartPrice":
            //    return "OBJ_3ATTR_14";
            //case "EndPrice":
            //    return "OBJ_3ATTR_15";
            default:
                return "OBJ_3ATTR_12";
        }
    }

    public List<Lot> getUserLots(String username, LotFilterDTO.Column sortCol) {
        User user = userDao.getTestUserByEmail(username);
        if (user == null) return null;
        return Arrays.asList(lotDao2.getUsersLots(user.getId(), getLotSortCol(sortCol)));
    }

    public List<Lot> getMyLots(String username, LotFilterDTO.Column sortCol) {
        com.main.sellplatform.entitymanager.testobj.User user = userDao.getTestUserByEmail(username);
        if (user == null) return null;
        return Arrays.asList(lotDao2.getUsersLots(user.getId(), getLotSortCol(sortCol)));
    }

    public List<Lot> getAllLots() {
        return lotDao.getPublishedLots();
    }

//    public Boolean addLot(Lot lot, String username) {
//        if (!validateLot(lot, username)) return false;
//        return lotDao.addLot(lot, username);
//    }
//
//    public Boolean updateLot(Lot lot, String username) {
//        if (!validateLot(lot, username)) return false;
//        return lotDao.updateLot(lot);
//    }

    public Boolean deleteLot(Long id, String username) {
        if (lotDao.getLot(id).getOwner() == null || !Objects.equals(lotDao.getLot(id).getOwner().getId(), userDao.getUserByEmail(username).getId())) {
            return false;
        }
        return lotDao.deleteLot(id);
    }

    public void createLot(Lot lot) {
        lotDao2.saveLot(lot);
    }

    public List<Lot> getPublishedLot(LotFilterDTO filterDTO) {
        List<Object> statements = new ArrayList<>();
        String filterWhere = filterDTO == null ? "" : getFilters(filterDTO, statements);
        return lotDao2.getAllLots(queries.whereBuyableLot() +(filterWhere.isEmpty() ? "" : filterWhere), statements);
    }

    public List<Lot> findLots(String keyword) {
        return lotDao.findPublishedLots(keyword);
    }

//    private Boolean validateLot(Lot lot, String username) {
//        return Objects.equals(lot.getOwner().getId(), userDao.getUserByEmail(username).getId())
//                && lot.getStartDate() != null
//                && !lot.getStartDate().isBefore(LocalDateTime.now())
//                && !lot.getStartDate().isAfter(lot.getEndDate())
//                && !lot.getTerm().isAfter(lot.getEndDate())
//                && !lot.getTerm().isBefore(lot.getStartDate());
//    }

    private String getFilters(LotFilterDTO filter, List<Object> statements) {
        StringBuilder filterWhere = new StringBuilder();

        List<LotFilterDTO.Category> categories = filter.getCategories();
        if (categories != null) {
            filterWhere.append(" AND lower(OBJ_3ATTR_18) IN (");
            for (LotFilterDTO.Category category : categories) {
                filterWhere.append("'").append(category.toString().toLowerCase()).append("',");
            }
            filterWhere.delete(filterWhere.length() - 1, filterWhere.length());
            filterWhere.append(") ");
        }

        Double minPrice = filter.getMinPrice();
        Double maxPrice = filter.getMaxPrice();
        if (!(minPrice == null && maxPrice == null)) {
            filterWhere.append(" AND ");
            if (minPrice == null || maxPrice == null) {
                if (minPrice == null) {
                    filterWhere.append("OBJ_3ATTR_14 < ").append(maxPrice);
                } else {
                    filterWhere.append("OBJ_3ATTR_14 > ").append(minPrice);
                }
            } else {
                filterWhere.append("OBJ_3ATTR_14 BETWEEN ").append(minPrice).append(" AND ").append(maxPrice);
            }
        }

        String search = filter.getSearch();
        if (search != null && !search.isEmpty()) {
            filterWhere.append(" AND (INSTR(OBJ_3ATTR_12, ?) > 0 OR INSTR(OBJ_3DESCR, ?) > 0)");
            statements.add(search);
            statements.add(search);
        }

        LotFilterDTO.Column sortColumn = filter.getSortColumn();
        Boolean direct = filter.getAsc();
        if (direct == null) direct = true;
        if (sortColumn == null) sortColumn = LotFilterDTO.Column.NAME;
        filterWhere.append(" ORDER BY ").append(getLotSortCol(sortColumn)).append(" ").append(direct ? "ASC" : "DESC");
        return filterWhere.toString();
    }

//    public List<Lot> getBuyableLots(LotFilterDTO filter) {
//        List<Object> statements = new ArrayList<>();
//        String filterWhere = filter == null ? "" : getFilters(filter, statements);
//        List<Lot> res = Arrays.asList(
//                lotDao2.getAllLots(queries.whereBuyableLot() + (filterWhere.isEmpty() ? "" : filterWhere), statements)
//        );
//        for (Lot lot : res) {
//            lot.setOwner(null);
//        }
//        return res;
//    }

    public Lot getLot(Long id) {
        return lotDao2.getLotById(id, null);
    }

    public Lot getBuyableLot(Long id) {
        Lot res = lotDao2.getLotById(id, "(OBJ_3ATTR_17 = 'NO BIDS' OR OBJ_3ATTR_17 = 'BIDDING')");
        if (res != null) res.setOwner(null);
        return res;
    }
}
